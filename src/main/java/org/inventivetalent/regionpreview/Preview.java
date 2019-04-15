package org.inventivetalent.regionpreview;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.NBTTag;
import org.inventivetalent.nbt.stream.NBTInputStream;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "RegionPreview",
					 description = "Utility to generated map-like previews of Minecraft region files",
					 abbreviateSynopsis = true,
					 versionProvider = VersionProvider.class,
					 showDefaultValues = true
)
public class Preview implements Callable<Boolean> {

	@CommandLine.Parameters(paramLabel = "FILES",
							description = "Files/Directory to generate previews for")
	private File[] input;

	@CommandLine.Option(names = { "-s", "--scale" },
						description = "Scale of the output image (1:?)")
	private int scale = 1;

	@CommandLine.Option(names = { "-o", "--output" },
						description = "Output directory for generated images")
	private File output = new File(".");

	@CommandLine.Option(names = { "--stitch" },
						description = "Whether to stitch the resulting images together into a large image, instead of outputting single files")
	private boolean stitch = false;

	@CommandLine.Option(names = { "--stitchWidth" },
						description = "Number of regions to combine in the x-direction")
	private int stitchWidth = 1;

	@CommandLine.Option(names = { "--stitchHeight" },
						description = "Number of regions to combine in the z-direction")
	private int stitchHeight = 1;

	@CommandLine.Option(names = { "--stitchStartX" },
						description = "Where to start stitching in x-direction")
	private int stitchStartX = 0;

	@CommandLine.Option(names = { "--stitchStartZ" },
						description = "Where to start stitching in z-direction")
	private int stitchStartZ = 0;

	private BufferedImage stitchedImage;

	@Override
	public Boolean call() throws Exception {
		if (input == null || input.length == 0) {
			System.err.println("No files specified");
			return false;
		}

		if (scale < 1) {
			System.err.println("Scale must be at least 1");
			return false;
		}
		System.out.println("Generating preview image(s) in scale 1:" + scale + "...");

		if (stitch) {
			float iScale = 1f / scale;

			int w = (int) Math.ceil((512 * iScale) * stitchWidth);
			int h = (int) Math.ceil((512 * iScale) * stitchHeight);

			System.out.println("Will be stitching together individual files. Final image will be " + w + "x" + h);

			stitchedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}

		boolean success = false;
		for (File file : input) {
			if (!file.exists()) {
				System.err.println("File " + file + " not found");
				continue;
			}

			if (file.isDirectory()) {
				if (processDirectory(file)) { success = true; }
			} else if (file.isFile()) {
				if (processFile(file)) { success = true; }
			}
		}

		if (stitch) {
			try (FileOutputStream out = new FileOutputStream(new File(output, "stitched-" + System.currentTimeMillis() + ".png"))) {
				ImageIO.write(stitchedImage, "png", out);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		System.out.println("Done!");

		return success;
	}

	boolean processDirectory(File dir) {
		boolean success = false;
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (processFile(file)) { success = true; }
			} else if (file.isDirectory()) {
				if (processDirectory(file)) { success = true; }
			}
		}

		return success;
	}

	boolean processFile(File file) {
		if (!file.getName().endsWith(".mca")) {
			System.err.println("File " + file + " does not appear to be a region file (no .mca extension) - skipping");
			return false;
		}
		System.out.println("Working on " + file + "...");

		if (stitch) {
			// Fingers crossed people always use regular region file formats
			String[] fileSplit = file.getName().split("\\.");
			int regionX = Integer.parseInt(fileSplit[1]);
			int regionZ = Integer.parseInt(fileSplit[2]);

			if (regionX < stitchStartX || regionX - stitchStartX >= stitchWidth) {
				return false;
			}
			if (regionZ < stitchStartZ || regionZ - stitchStartZ >= stitchHeight) {
				return false;
			}

			try {
				makeImageForRegionFile(file, stitchedImage, scale, regionX - stitchStartX, regionZ - stitchStartZ);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			File outFile = new File(output, file.getPath() + ".png");
			if (outFile.exists()) {
				return false;
			}

			BufferedImage image;
			try {
				image = makeImageForRegionFile(file, scale);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			try (FileOutputStream out = new FileOutputStream(outFile)) {
				ImageIO.write(image, "png", out);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public static BufferedImage makeImageForRegionFile(File file, int scale) throws Exception {
		float iScale = 1f / scale;
		return makeImageForRegionFile(file, new BufferedImage((int) Math.floor(512 * iScale), (int) Math.floor(512 * iScale), BufferedImage.TYPE_INT_RGB), scale, 0, 0);
	}

	public static BufferedImage makeImageForRegionFile(File file, BufferedImage image, int scale, int xOffset, int zOffset) throws Exception {
		float iScale = 1f / scale;

		try (RegionFile regionFile = new RegionFile(file)) {
			for (int x = 0; x < 32; x++) {
				for (int z = 0; z < 32; z++) {
					if (regionFile.hasChunk(x, z)) {
						try (DataInputStream dataIn = regionFile.getChunkDataInputStream(x, z)) {
							if (dataIn != null) {
								try (NBTInputStream nbtIn = new NBTInputStream(dataIn)) {
									CompoundTag rootTag = (CompoundTag) nbtIn.readNBTTag();
									if (rootTag != null) {
										Chunk chunk;
										if (rootTag.has("DataVersion")) {// 1.13+
											NBTTag dataVersionTag = rootTag.get("DataVersion");

											CompoundTag levelTag = rootTag.getCompound("Level");
											chunk = new Chunk13(levelTag);
										} else {// Pre 1.13
											CompoundTag levelTag = rootTag.getCompound("Level");
											chunk = new Chunk12(levelTag);
										}

										for (int xx = 0; xx < 16; xx += scale) {
											for (int zz = 0; zz < 16; zz += scale) {

												int highest = chunk.getHighestBlockAt(xx, zz);
												int color = 0;
												if (highest > 0) {
													color = chunk.getColorAt(xx, highest, zz);
												}

												float fX = (xx + x * 16 + xOffset * 512) * iScale;
												float fZ = (zz + z * 16 + zOffset * 512) * iScale;
												image.setRGB((int) Math.floor(fX), (int) Math.floor(fZ), color);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return image;
	}

}
