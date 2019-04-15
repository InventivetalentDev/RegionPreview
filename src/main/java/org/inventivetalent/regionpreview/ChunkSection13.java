package org.inventivetalent.regionpreview;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.ListTag;
import org.inventivetalent.nbt.LongArrayTag;

import java.util.BitSet;

public class ChunkSection13 extends ChunkSection {

	public ChunkSection13(CompoundTag tag) {
		super(tag);
		String[] materialNames = new String[4096];

		ListTag<CompoundTag> paletteList = tag.getList("Palette");
		String[] palette = new String[paletteList.size()];
		for (int i = 0; i < paletteList.size(); i++) {
			CompoundTag paletteItem = paletteList.get(i);
			palette[i] = paletteItem.get("Name").getAsString();
		}

		long[] blockStates = ((LongArrayTag) tag.get("BlockStates")).getValue();

		int wordSize = blockStates.length * 64 / 4096;
		if (wordSize == 4) {
			// Optimised special case
			for (int w = 0; w < 4096; w += 16) {
				long data = blockStates[w >> 4];
				materialNames[w] = palette[(int) (data & 0xf)];
				materialNames[w + 1] = palette[(int) ((data & 0xf0) >> 4)];
				materialNames[w + 2] = palette[(int) ((data & 0xf00) >> 8)];
				materialNames[w + 3] = palette[(int) ((data & 0xf000) >> 12)];
				materialNames[w + 4] = palette[(int) ((data & 0xf0000) >> 16)];
				materialNames[w + 5] = palette[(int) ((data & 0xf00000) >> 20)];
				materialNames[w + 6] = palette[(int) ((data & 0xf000000) >> 24)];
				materialNames[w + 7] = palette[(int) ((data & 0xf0000000L) >> 28)];
				materialNames[w + 8] = palette[(int) ((data & 0xf00000000L) >> 32)];
				materialNames[w + 9] = palette[(int) ((data & 0xf000000000L) >> 36)];
				materialNames[w + 10] = palette[(int) ((data & 0xf0000000000L) >> 40)];
				materialNames[w + 11] = palette[(int) ((data & 0xf00000000000L) >> 44)];
				materialNames[w + 12] = palette[(int) ((data & 0xf000000000000L) >> 48)];
				materialNames[w + 13] = palette[(int) ((data & 0xf0000000000000L) >> 52)];
				materialNames[w + 14] = palette[(int) ((data & 0xf00000000000000L) >> 56)];
				materialNames[w + 15] = palette[(int) ((data & 0xf000000000000000L) >>> 60)];
			}
		} else {
			BitSet bitSet = BitSet.valueOf(blockStates);
			for (int w = 0; w < 4096; w++) {
				int wordOffset = w * wordSize;
				int index = 0;
				for (int b = 0; b < wordSize; b++) {
					index |= bitSet.get(wordOffset + b) ? 1 << b : 0;
				}
				materialNames[w] = palette[index];
			}
		}

		for (int i = 0; i < 4096; i++) {
			String shortName = materialNames[i].substring("minecraft:".length());
			colors[i] = COLORS_BY_NAME.getOrDefault(shortName, 0);
		}
	}

}
