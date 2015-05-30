package com.firstarchon.arcana.block;

import java.util.List;
import java.util.Random;
import com.firstarchon.arcana.referance.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This is a block class that I made that allows you to create your own multi-textured blocks.
 * Some classes may not be available with this class(e.g. FurnaceFuel, Constants), but they are
 * easy enough for you to create and implement/work around on your own. :)
 * @author Brian_Entei
 */
public class MultiTexturedBlock extends Block {
	protected String CodeName;
	protected boolean initialized = false;
	protected int renderID = 0;//Default for stone blocks, ore, etc

	protected Item itemDropped;
	protected int amountDropped = 1;
	protected int expDropped = 0;

	protected boolean isSolid = true;
	private boolean isFurnaceFuel;

	protected boolean canPlaceTorchOnTop = true;
	protected boolean canPlaceTorchOnBlock = true;

	protected boolean isGrassBlock;
	protected Block baseDirtBlock;

	protected boolean isDirtBlock;
	protected boolean isWoodBlock;

	protected boolean isIngotBlock;
	protected ItemStack ingotItem;

	public MultiTexturedBlock initialize() {
		this.itemDropped = Item.getItemFromBlock(this);
		this.initialized = true;
		return this;
	}

	public MultiTexturedBlock(String CodeName) {
		this(CodeName, Material.ground);
	}

	public MultiTexturedBlock(String CodeName, Material material) {
		super(material);
		this.CodeName = CodeName;
		this.setStoneBlock().setCreativeTab(CreativeTabs.tabBlock).setBlockName(Reference.MOD_ID + "_" + CodeName).setBlockTextureName(Reference.MOD_ID + ":" + CodeName);
		//GameRegistry.registerBlock(this, CodeName);
	}

	public MultiTexturedBlock setRenderType(int renderID) {
		this.renderID = renderID;
		return this;
	}

	@Override
	public int getRenderType() {
		return (this.isMultiTextured ? (this.canChangeSides ? this.renderID : super.getRenderType()) : super.getRenderType());
	}

	@Override
	public boolean isNormalCube() {
		return this.blockMaterial.isOpaque() && this.isSolid && !this.canProvidePower();
	}

	/**
	 * Convenience method for rendering classes; don't remove
	 */
	public static int getBlockOrientation(int p_150076_0_) {
		return p_150076_0_ & 7;
	}

	@Override
	public MultiTexturedBlock setBlockTextureName(String CodeName) {
		super.setBlockTextureName(Reference.MOD_ID + ":" + CodeName);
		return this;
	}

	@Override
	public MultiTexturedBlock setCreativeTab(CreativeTabs creativeTab) {
		super.setCreativeTab(creativeTab);
		return this;
	}

	public MultiTexturedBlock setItemDropped(Item item) {
		this.itemDropped = item;
		return this;
	}

	public MultiTexturedBlock setItemDropped(Item item, int amountDropped) {
		this.itemDropped = item;
		this.amountDropped = amountDropped;
		return this;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return this.amountDropped;// + (this.dropRandomExtraAmount ? p_149745_1_.nextInt(3) : 0);
	}

	/**Enables this block to be smelted in a furnace into another item
	 * @param output The item or block that comes out of the furnace
	 * @param xp The amount of experience the player receives upon removal of the output item
	 */
	public MultiTexturedBlock addSmeltingRecipe(ItemStack output, float xp) {
		//GameRegistry.addSmelting(this, output, xp);
		return this;
	}
/*
	public MultiTexturedBlock setCanBeFurnaceFuel(boolean canBeFurnaceFuel, int burnTime) {
		if(canBeFurnaceFuel) {
			FurnaceFuel.addBurnableItem(new ItemStack(this), burnTime);
		} else {
			FurnaceFuel.removeBurnableItem(new ItemStack(this));
		}
		this.isFurnaceFuel = canBeFurnaceFuel;
		return this;
	}
*/
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return this.isSolid;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y, z);
		return super.canPlaceBlockOnSide(world, x, y, z, side);
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block instanceof MultiTexturedBlock) {
			MultiTexturedBlock b = ((MultiTexturedBlock) block);
			if(b.canPlaceTorchOnBlock) {
				return true;
			}
			return b.isSideSolid(world, x, y, z, ForgeDirection.UP);
		}
		return false;
	}

	public MultiTexturedBlock setCanPlaceTorchOnTop(boolean canPlaceTorchOnTop) {
		this.canPlaceTorchOnBlock = canPlaceTorchOnTop;
		return this;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return this.isIngotBlock;
	}

	/**
	 * Very useful method for adding item recipes automatically and changing this blocks hardness and sound type.
	 * Also registers this block as an acceptable base block for beacons(you can use this block to build a beacon pyramid if you set this to true)
	 */
	@SuppressWarnings("boxing")
	public MultiTexturedBlock setIngotBlock(boolean isIngotBlock, ItemStack itemStack, int harvestLevel) {
		if(isIngotBlock) {
			this.ingotItem = itemStack;
			this.setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal);
			//GameRegistry.addRecipe(new ItemStack(this), new Object[] {"###", "###", "###", '#', new ItemStack(this.ingotItem.getItem())});
			//GameRegistry.addRecipe(new ItemStack(this.ingotItem.getItem(), this.ingotItem.stackSize), new Object[] {"#", '#', this});
			this.setHarvestLevel("pickaxe", harvestLevel);
			this.isIngotBlock = true;
		}
		return this;
	}

	/**
	 * Very useful method for adding item recipes automatically and changing this blocks hardness and sound type.
	 * Also registers this block as an acceptable base block for beacons(you can use this block to build a beacon pyramid if you set this to true)
	 */
	public MultiTexturedBlock setIngotBlock(boolean isIngotBlock, ItemStack itemStack) {
		return this.setIngotBlock(isIngotBlock, itemStack, 1);
	}

	public MultiTexturedBlock setStoneBlock() {
		this.setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston);
		this.setHarvestLevel("pickaxe", 0);
		return this;
	}

	public MultiTexturedBlock setGrassBlock() {
		this.setHardness(0.6F).setStepSound(soundTypeGrass);
		this.setHarvestLevel("shovel", 0);
		this.isGrassBlock = true;
		this.baseDirtBlock = null;
		if(this.itemDropped == null) {
			this.setItemDropped(Item.getItemFromBlock(this));
		}
		return this;
	}

	public MultiTexturedBlock setGrassBlock(Block baseDirtBlock) {
		this.setHardness(0.6F).setStepSound(soundTypeGrass);
		this.setHarvestLevel("shovel", 0);
		this.isGrassBlock = true;
		this.baseDirtBlock = baseDirtBlock;
		if(this.itemDropped == null) {
			this.setItemDropped(Item.getItemFromBlock(baseDirtBlock));
		}
		return this;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		if(this.isGrassBlock) {
			if(this.baseDirtBlock != null) {
				return Item.getItemFromBlock(this.baseDirtBlock);
			}
		}
		if(this.itemDropped == null) {
			return Item.getItemFromBlock(this);
		}
		return this.itemDropped;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if(!this.isGrassBlock) {return;}
		if(this.baseDirtBlock == null) {return;}
		if(this.baseDirtBlock == this) {return;}
		if(!world.isRemote) {
			if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
				world.setBlock(x, y, z, this.baseDirtBlock);
			} else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
				for (int l = 0; l < 4; ++l) {
					int i1 = x + random.nextInt(3) - 1;
					int j1 = y + random.nextInt(5) - 3;
					int k1 = z + random.nextInt(3) - 1;
					Block block = world.getBlock(i1, j1 + 1, k1);
					if(world.getBlock(i1, j1, k1) == this.baseDirtBlock && world.getBlockMetadata(i1, j1, k1) == 0 && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
						world.setBlock(i1, j1, k1, this);
					}
				}
			}
		}
	}

	public MultiTexturedBlock setDirtBlock() {
		this.setHardness(0.5F).setStepSound(soundTypeGravel);
		this.setHarvestLevel("shovel", 0);
		this.baseDirtBlock = this;
		return this;
	}

	public MultiTexturedBlock setWoodBlock() {
		this.setHardness(2.5F).setStepSound(soundTypeWood);
		this.setHarvestLevel("axe", 0);
		this.isWoodBlock = true;
		return this;
	}

	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		return this.isWoodBlock;
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return this.expDropped;
	}

	public MultiTexturedBlock setExpDrop(int expDrop) {
		this.expDropped = expDrop;
		return this;
	}

	public MultiTexturedBlock setIsSlippery(boolean isSlippery) {
		this.slipperiness = (isSlippery ? 1.0F : 0.6F);
		return this;
	}

	public MultiTexturedBlock setCanBlockGrass(boolean canBlockGrass) {
		this.canBlockGrass = canBlockGrass;
		return this;
	}

	public MultiTexturedBlock setIsOpaqueCube(boolean isOpaqueCube) {
		this.opaque = isOpaqueCube;
		this.lightOpacity = this.opaque ? 255 : 0;
		return this;
	}

	/**
	 * @param lightLevel integer value 0 - 15
	 */
	public MultiTexturedBlock setLightLevel(int lightLevel) {
		if(lightLevel >= 0 && lightLevel <= 15) {
			this.lightValue = lightLevel;
		} else {
			System.err.println("Warning: The light level of block \"" + this.getUnlocalizedName() + "\" cannot be set to \"" + lightLevel + "\"; it must be an integer value of 0 to 15.");
		}
		return this;
	}

	/**
	 * @param lightOpacity integer value 0(transparent) - 255(opaque) 
	 */
	@Override
	public MultiTexturedBlock setLightOpacity(int lightOpacity) {
		if(lightOpacity >= 0 && lightOpacity <= 255) {
			this.lightOpacity = lightOpacity;
		} else {
			System.err.println("Warning: The light opacity value of block \"" + this.getUnlocalizedName() + "\" cannot be set to \"" + lightOpacity + "\"; it must be an integer value of 0(transparent) to 255(opaque).");
		}
		return this;
	}

	protected boolean isMultiTextured = false;
	public MultiTexturedBlock setIsMultiTextured(boolean isMultiTextured) {
		this.isMultiTextured = isMultiTextured;
		return this;
	}

	protected boolean canChangeSides = false;
	public MultiTexturedBlock setCanSideChangeToPlayerView(boolean canChangeSides) {
		this.canChangeSides = canChangeSides;
		return this;
	}

	@SideOnly(Side.CLIENT)
	protected IIcon[] sides;
	public String[] sidesToLoad = new String[] {"", "", "", "", "", ""};

	/**
	 * This allows you to override the texture of a specific side of this block. 'side' is an integer value ranging from 0 - 5
	 */
	public MultiTexturedBlock setBlockSideIIcon(int side, String fullTexturePath) {
		if(side > 5 || side < 0) {return this;}
		this.sidesToLoad[side] = fullTexturePath;
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + this.CodeName);
		this.sides = new IIcon[] {
				iconRegister.registerIcon((this.sidesToLoad[0].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_bottom") : this.sidesToLoad[0])),
				iconRegister.registerIcon((this.sidesToLoad[1].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_top") : this.sidesToLoad[1])),
				iconRegister.registerIcon((this.sidesToLoad[2].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_north") : this.sidesToLoad[2])),
				iconRegister.registerIcon((this.sidesToLoad[3].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_south") : this.sidesToLoad[3])),
				iconRegister.registerIcon((this.sidesToLoad[4].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_west") : this.sidesToLoad[4])),
				iconRegister.registerIcon((this.sidesToLoad[5].isEmpty() ? (Reference.MOD_ID + ":" + this.CodeName + "_east") : this.sidesToLoad[5]))
		};
		System.out.println("Registered block icons for block \"" + this.CodeName + "\".");
	}

	public String getUnlocalizedName(ItemStack stack) {
		switch(stack.getItemDamage()) {
		case 0:
			return this.getUnlocalizedName() + "_bottom";
		case 1:
			return this.getUnlocalizedName() + "_top";
		case 2:
			return this.getUnlocalizedName() + "_north";
		case 3:
			return this.getUnlocalizedName() + "_south";
		case 4:
			return this.getUnlocalizedName() + "_west";
		case 5:
			return this.getUnlocalizedName() + "_east";
		default:
			return this.getUnlocalizedName();
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return (this.isMultiTextured ? !this.canChangeSides : super.renderAsNormalBlock());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if(!this.canChangeSides) {
			return this.sides[side];
		}
		switch(meta) {
			case 0 :
				switch(side) {
					case 0 : return this.sides[1];
					case 1 : return this.sides[0];
					case 2 : return this.sides[3];
					case 3 : return this.sides[2];
					case 4 : return this.sides[4];
					case 5 : return this.sides[5];
				}
			case 1 :
				return this.sides[side];
			case 2 :
				switch(side) {
					case 0 : return this.sides[3];
					case 1 : return this.sides[2];
					case 2 : return this.sides[1];
					case 3 : return this.sides[0];
					case 4 : return this.sides[5];
					case 5 : return this.sides[4];
				}
			case 3 :
				switch(side) {
					case 0 : return this.sides[3];
					case 1 : return this.sides[2];
					case 2 : return this.sides[0];
					case 3 : return this.sides[1];
					case 4 : return this.sides[4];
					case 5 : return this.sides[5];
				}
			case 4 :
				switch(side) {
					case 0 : return this.sides[3];
					case 1 : return this.sides[2];
					case 2 : return this.sides[4];
					case 3 : return this.sides[5];
					case 4 : return this.sides[1];
					case 5 : return this.sides[0];
				}
			case 5 :
				switch(side) {
					case 0 : return this.sides[3];
					case 1 : return this.sides[2];
					case 2 : return this.sides[5];
					case 3 : return this.sides[4];
					case 4 : return this.sides[0];
					case 5 : return this.sides[1];
				}
		}
		System.err.println("int side: \"" + side + "\"; int meta: \"" + meta + "\" was not registered for the block \"" + this.getUnlocalizedName() + "\"!");
		return blockIcon;
	}

	protected boolean showSubBlocks = false;
	public MultiTexturedBlock setShowSubBlocks(boolean showSubBlocks) {
		this.showSubBlocks = showSubBlocks;
		return this;
	}
	public boolean getCanShowSubBlocks() {
		return this.showSubBlocks;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		if(this.canChangeSides && this.showSubBlocks) {
			for(int i = 0; i < 6; i++) {list.add(new ItemStack(item, 1, i));}
		} else {
			super.getSubBlocks(item, tab, list);
			return;
		}
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		int l = determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
		par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
		//int[] j = determineOrientationAndSides(par1World, par2, par3, par4, par5EntityLivingBase);
		//System.out.println("\"l\" var: \"" + l + "\"; metadata for block: pitch: " + j[0] + "; yaw/roll: " + j[1] + ";");
	}

	public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityLivingBase) {
		if(MathHelper.abs((float)par4EntityLivingBase.posX - par1) < 2.0F && MathHelper.abs((float)par4EntityLivingBase.posZ - par3) < 2.0F) {
			double d0 = par4EntityLivingBase.posY + 1.82D - par4EntityLivingBase.yOffset;
			if(d0 - par2 > 2.0D) {
				return 1;
			}
			if(par2 - d0 > 0.0D) {
				return 0;
			}
		}
		int l = MathHelper.floor_double((par4EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
	}

	/**
	 * If Minecraft allowed for two metadata tags to be used, a method like this one could be used to decide the horizontal rotation of blocks that are facing up or down; thus allowing for more than 6 different block rotations in game. For example, if you look down and jump-place a block, it's top texture will always be facing north, no matter what compass direction you were facing(yes you were facing down, but you were also facing in a compass direction - press f3 and have a look :p). So, with this, you would be able to set which compass direction the top(or bottom when looking up) texture(and therefore the rest of the block accordingly) is rotated towards, so that it isn't stuck on north all the time!<br>
	 * Example use of this method:<br>
	 * {@code int[] metadata = determineOrientationAndSides(world, x, y, z, entityLivingBase);}<br>
	 * {@code int pitch = metadata[0];//0 for when the player is looking up; 1 for when the player is looking down; 2 for when the player is not looking either up or down}<br>
	 * {@code int yawRoll = metadata[1];//This would be the same as the vanilla kind of metadata(values 2 - 5, same as before, with 2 being the player facing south and 5 being the player facing west)}<br>
	 */
	public static int[] determineOrientationAndSides(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityLivingBase) {
		int downUpMiddle = 2;
		if(MathHelper.abs((float)par4EntityLivingBase.posX - par1) < 2.0F && MathHelper.abs((float)par4EntityLivingBase.posZ - par3) < 2.0F) {
			double d0 = par4EntityLivingBase.posY + 1.82D - par4EntityLivingBase.yOffset;
			if(d0 - par2 > 2.0D) {
				downUpMiddle = 1;//player is looking down, so we make the block face up
			}
			if(par2 - d0 > 0.0D) {
				downUpMiddle = 0;//player is looking up, so we make the block face down
			}
			if(!(d0 - par2 > 2.0D) && !(par2 - d0 > 0.0D)) {
				downUpMiddle = 2;//player is looking relatively straight; so use the yaw/roll integer instead
			}
		}
		int l = MathHelper.floor_double((par4EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return new int[] {downUpMiddle, (l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : (downUpMiddle != 2 ? downUpMiddle : 0)))))};
	}

}