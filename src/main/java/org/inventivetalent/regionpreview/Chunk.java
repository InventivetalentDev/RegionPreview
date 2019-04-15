package org.inventivetalent.regionpreview;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.ListTag;

public class Chunk {

	ChunkSection[] sections;

	public Chunk(CompoundTag levelTag) {
		ListTag<CompoundTag> sectionsTag = levelTag.getList("Sections");
		this.sections = new ChunkSection[sectionsTag.size()];

		for (int i = 0; i < sectionsTag.size(); i++) {
			this.sections[i] = new ChunkSection(sectionsTag.get(i));
		}
	}

	public int getHighestColorAt(int x, int z) {
		int highest = getHighestBlockAt(x, z);
		if(highest>=0) {
			return getColorAt(x, highest, z);
		}
		return 0;
	}

	public int getColorAt(int x, int y, int z) {
		ChunkSection section = sections[y >> 4];
		if (section == null) { return 0; }
		return section.colors[blockOffset(x, y, z)];
	}

	public int getHighestBlockAt(int x, int z) {
		for (int yy = sections.length - 1; yy >= 0; yy--) {
			if (sections[yy] != null) {
				int[] colors = sections[yy].colors;
				final int base = blockOffset(x, 0, z);
				for (int i = blockOffset(x, 15, z); i >= base; i -= 256) {
					if (colors[i] > 0) {
						return (yy << 4) | ((i - base) >> 8);
					}
				}
			}
		}
		return -1;
	}

	static int blockOffset(int x, int y, int z) {
		return x | ((z | ((y & 0xF) << 4)) << 4);
	}

}
