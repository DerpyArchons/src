package block;

import com.firstarchon.arcana.referance.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockArcanea extends Block
{
	public BlockArcanea(String baseName ,Material material) {
		  super(material);
		setBlockName(Reference.MOD_ID + ":" + baseName);
	    setBlockTextureName(Reference.MOD_ID + ":" + baseName);
	  
	   	}

}
