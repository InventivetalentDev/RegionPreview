package org.inventivetalent.regionpreview;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.ListTag;

public class Chunk12 extends Chunk{

	public Chunk12(CompoundTag levelTag) {
		super();
		ListTag<CompoundTag> sectionsTag = levelTag.getList("Sections");
		this.sections = new ChunkSection[sectionsTag.size()];
		for (int i = 0; i < sectionsTag.size(); i++) {
			this.sections[i] = new ChunkSection12(sectionsTag.get(i));
		}
	}

}
