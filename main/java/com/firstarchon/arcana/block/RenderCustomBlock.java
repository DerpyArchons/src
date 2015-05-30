package com.firstarchon.arcana.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**@author Brian_Entei */
public class RenderCustomBlock implements ISimpleBlockRenderingHandler {
	protected int renderID;
	protected boolean render3DInInventory = true;
	protected boolean flipTextureHorizontally = false;
	protected boolean flipTextureVertically = false;

	/**
	 * A rendering class for rendering simple blocks. This renderer fixes any rotational/flipping bugs that exist in the vanilla rendering code, such as the bottom texture being flipped.
	 * @author Brian_Entei
	 */
	public RenderCustomBlock() {}

	public RenderCustomBlock setShouldRender3DInInventory(boolean render3DInInventory) {
		this.render3DInInventory = render3DInInventory;
		return this;
	}

	public RenderCustomBlock initialize() {
		//this.renderID = RenderingRegistry.getNextAvailableRenderId();
		//RenderingRegistry.registerBlockHandler(this);
		return this;
	}

	/**
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderInventoryBlock(net.minecraft.block.Block, int, int, net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		renderCustomBlockWithMetaDataInInventory(block, metadata, modelId, renderer, true);
	}

	/**
	 * @see RenderStandardBlock#renderInventoryBlock(Block, int, int, RenderBlocks)
	 */
	public void renderCustomBlockWithMetaDataInInventory(Block block, int metadata, int modelId, RenderBlocks renderer, boolean overrideMetadata) {
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		if(overrideMetadata) {
			metadata = 1;
		}
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);//Rotates icon so that the south and west sides of the block are facing the camera with the top side on top(this is vanilla block icon behaviour)
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);//Moves icon into center of slot in inventory(we don't want a block that appears to be floating in our inventory now do we? lol maybe we do! jk...)

		Tessellator tessellator = Tessellator.instance;
		
		//The following two variables are variables from {@link net.minecraft.client.renderer.ItemRenderer#renderItem(EntityLivingBase, ItemStack, int, ItemRenderType)}: 
		int itemStackDamage = metadata;//0; //We can(and should) use metadata instead of just 0.
		float p_147800_3_ = 1.0F; //1.0F is what the method from the ItemRenderer class used, so we'll use that here too.

		int j;
		float f1;
		float f2;
		float f3;
		int k;

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		this.renderFaceYNeg(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, itemStackDamage));//Bottom
		tessellator.draw();

		boolean flag = (block.getMaterial().equals(Material.grass) && renderer.useInventoryTint);

		if(flag) {
			k = block.getRenderColor(itemStackDamage);
			f2 = (k >> 16 & 255) / 255.0F;
			f3 = (k >> 8 & 255) / 255.0F;
			float f4 = (k & 255) / 255.0F;
			GL11.glColor4f(f2 * p_147800_3_, f3 * p_147800_3_, f4 * p_147800_3_, 1.0F);
		}

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		this.renderFaceYPos(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, itemStackDamage));//Top
		tessellator.draw();

		if(flag) {
			GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F);
		}

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		this.renderFaceZNeg(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, itemStackDamage));//
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		this.renderFaceZPos(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, itemStackDamage));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		this.renderFaceXNeg(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, itemStackDamage));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		this.renderFaceXPos(renderer, block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, itemStackDamage));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	/**
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderWorldBlock(net.minecraft.world.IBlockAccess, int, int, int, net.minecraft.block.Block, int, net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return this.renderStandardBlock(renderer, block, x, y, z);//This method is a re-make of the vanilla method RenderBlocks.renderStandardBlock(block, x, y, z). It is used to get to the methods starting with 'renderFace****()', which have bugfixes and added features in them.
		
		//You could also call this.renderMultiTexturedBlockWithMetadata(...) to render a multi-textured block that can be placed in the world to face any direction, or
		//you could just call this.renderMultiTexturedBlock(...) to just render a multi-textured block in the world.
	}

	/**
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#shouldRender3DInInventory(int)
	 */
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return this.render3DInInventory;
	}

	/**
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#getRenderId()
	 */
	@Override
	public int getRenderId() {
		return this.renderID;
	}

	/**
	 * Usage in code:<br>
	 * {@code boolean someboolean = shouldSideBeFlipped[meta][side][i];} (where 'i' can be zero for a horizontal texture flip, or 1 for a vertical texture flip)<br>
	 * Used internally by the class {@link #RenderStandardBlock()} to fix buggy vanilla texture behaviour on certain sides(namely, the bottom surface of any vanilla block)
	 * @author Brian_Entei
	 */
	public static final boolean[][][] shouldSideBeFlipped = new boolean[][][] {
			{//Meta = 0
				{true, false},  //side = 0
				{false, false}, //side = 1
				{false, false}, //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{false, false}  //side = 5
			},
			{//Meta = 1
				{true, false},  //side = 0
				{false, false}, //side = 1
				{false, false}, //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{false, false}  //side = 5
			},
			{//Meta = 2
				{true, false},  //side = 0
				{false, false}, //side = 1
				{false, false}, //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{true, false}   //side = 5
			},
			{//Meta = 3
				{true, false},  //side = 0
				{false, false}, //side = 1
				{false, false}, //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{true, false}   //side = 5
			},
			{//Meta = 4
				{true, false},  //side = 0
				{false, false}, //side = 1
				{true, false},  //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{false, false}  //side = 5
			},
			{//Meta = 5
				{true, false},  //side = 0
				{false, false}, //side = 1
				{true, false},  //side = 2
				{false, false}, //side = 3
				{false, false}, //side = 4
				{false, false}  //side = 5
			}
	};

	/**
	 * Helper method for determining if the texture of a specific side of a block should be flipped horizontally, vertically, or both.
	 * @param renderer
	 * @param metadata
	 * @param side
	 * @author Brian_Entei
	 */
	private void determineIfSideShouldBeFlipped(RenderBlocks renderer, int metadata, int side) {
		this.flipTextureHorizontally = shouldSideBeFlipped[metadata][side][0];
		this.flipTextureVertically = shouldSideBeFlipped[metadata][side][1];
	}

	/**
	 * Use this method to render a block with up to 6 different textures in the world.
	 */
	public boolean renderMultiTexturedBlock(int x, int y, int z, Block block, int metadata, int modelId, RenderBlocks renderer) {
		switch(metadata) {
		case 0:
			renderer.uvRotateEast = 3;
			renderer.uvRotateWest = 3;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateNorth = 3;
		case 1:
			//Nothing needed here; This is the default metadata value for multi-textured blocks!
		default:
			break;
		case 2:
			renderer.uvRotateSouth = 1;
			renderer.uvRotateNorth = 2;
			break;
		case 3:
			renderer.uvRotateSouth = 2;
			renderer.uvRotateNorth = 1;
			renderer.uvRotateTop = 3;
			renderer.uvRotateBottom = 3;
			break;
		case 4:
			renderer.uvRotateEast = 1;
			renderer.uvRotateWest = 2;
			renderer.uvRotateTop = 2;
			renderer.uvRotateBottom = 1;
			break;
		case 5:
			renderer.uvRotateEast = 2;
			renderer.uvRotateWest = 1;
			renderer.uvRotateTop = 1;
			renderer.uvRotateBottom = 2;
		}
		this.renderStandardBlock(renderer, block, x, y, z);
		renderer.uvRotateEast = 0;
		renderer.uvRotateWest = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;
		return true;
	}

	/**
	 * Use this method to render a block with up to 6 different textures in the world that can also be placed in any direction via metadata(see my block class for examples of this).
	 */
	public boolean renderMultiTexturedBlockWithMetadata(int x, int y, int z, Block block, int metadata, int modelId, RenderBlocks renderer) {
		switch(metadata) {
		case 0:
			renderer.uvRotateEast = 3;
			renderer.uvRotateWest = 3;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateNorth = 3;
		case 1:
			//Nothing needed here; This is the default metadata value for multi-textured blocks!
		default:
			break;
		case 2:
			renderer.uvRotateSouth = 1;
			renderer.uvRotateNorth = 2;
			break;
		case 3:
			renderer.uvRotateSouth = 2;
			renderer.uvRotateNorth = 1;
			renderer.uvRotateTop = 3;
			renderer.uvRotateBottom = 3;
			break;
		case 4:
			renderer.uvRotateEast = 1;
			renderer.uvRotateWest = 2;
			renderer.uvRotateTop = 2;
			renderer.uvRotateBottom = 1;
			break;
		case 5:
			renderer.uvRotateEast = 2;
			renderer.uvRotateWest = 1;
			renderer.uvRotateTop = 1;
			renderer.uvRotateBottom = 2;
		}
		this.renderStandardBlockWithMetadata(renderer, metadata, block, x, y, z);
		renderer.uvRotateEast = 0;
		renderer.uvRotateWest = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;
		return true;
	}

	//Copied vanilla sources beyond this point(Of course I had to add the parameter "renderer" to the methods and replace references of 'this' to 'renderer' manually):

	/**
	 * Renders a standard cube block with metadata at the given coordinates (WITH FIXED flipped textures!!!1 by Brian_Entei)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlock(RenderBlocks renderer, Block block, int x, int y, int z) {
		int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}
		return Minecraft.isAmbientOcclusionEnabled() && block.getLightValue() == 0 ? (renderer.partialRenderBounds ? this.renderStandardBlockWithAmbientOcclusionPartial(renderer, block, x, y, z, f, f1, f2) : this.renderStandardBlockWithAmbientOcclusion(renderer, block, x, y, z, f, f1, f2)) : this.renderStandardBlockWithColorMultiplier(renderer, block, x, y, z, f, f1, f2);
	}

	/**
	 * Renders a standard cube block with ambient occlusion. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, red, green, blue (lighting)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithAmbientOcclusion(RenderBlocks renderer, Block p_147751_1_, int p_147751_2_, int p_147751_3_, int p_147751_4_, float p_147751_5_, float p_147751_6_, float p_147751_7_) {
		renderer.enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);
		
		if (renderer.getBlockIcon(p_147751_1_).getIconName().equals("grass_top"))
		{
			flag1 = false;
		}
		else if (renderer.hasOverrideBlockTexture())
		{
			flag1 = false;
		}
		
		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;
		
		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_, 0))
		{
			if (renderer.renderMinY <= 0.0D)
			{
				--p_147751_3_;
			}
			
			renderer.aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();
			
			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (renderer.renderMinY <= 0.0D)
			{
				++p_147751_3_;
			}

			i1 = l;

			if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.5F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_, 1))
		{
			if (renderer.renderMaxY >= 1.0D)
			{
				++p_147751_3_;
			}

			renderer.aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (renderer.renderMaxY >= 1.0D)
			{
				--p_147751_3_;
			}

			i1 = l;

			if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_;
			renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_;
			renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_;
			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1, 2))
		{
			if (renderer.renderMinZ <= 0.0D)
			{
				--p_147751_4_;
			}

			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			renderer.aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (renderer.renderMinZ <= 0.0D)
			{
				++p_147751_4_;
			}

			i1 = l;

			if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
			f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 2);
			this.renderFaceZNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceZNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1, 3))
		{
			if (renderer.renderMaxZ >= 1.0D)
			{
				++p_147751_4_;
			}

			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (renderer.renderMaxZ >= 1.0D)
			{
				--p_147751_4_;
			}

			i1 = l;

			if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
			f6 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f5 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3);
			this.renderFaceZPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3));

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceZPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_, 4))
		{
			if (renderer.renderMinX <= 0.0D)
			{
				--p_147751_2_;
			}

			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
			}

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
			}

			if (renderer.renderMinX <= 0.0D)
			{
				++p_147751_2_;
			}

			i1 = l;

			if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
			f3 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 4);
			this.renderFaceXNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceXNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_, 5))
		{
			if (renderer.renderMaxX >= 1.0D)
			{
				++p_147751_2_;
			}

			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
			}

			if (renderer.renderMaxX >= 1.0D)
			{
				--p_147751_2_;
			}

			i1 = l;

			if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
			f5 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
			f6 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 5);
			this.renderFaceXPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceXPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		renderer.enableAO = false;
		return flag;
	}

	/**
	 * Renders non-full-cube block with ambient occlusion. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, red, green, blue (lighting)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithAmbientOcclusionPartial(RenderBlocks renderer, Block p_147808_1_, int p_147808_2_, int p_147808_3_, int p_147808_4_, float p_147808_5_, float p_147808_6_, float p_147808_7_)
	{
		renderer.enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);

		if (renderer.getBlockIcon(p_147808_1_).getIconName().equals("grass_top"))
		{
			flag1 = false;
		}
		else if (renderer.hasOverrideBlockTexture())
		{
			flag1 = false;
		}

		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_, 0))
		{
			if (renderer.renderMinY <= 0.0D)
			{
				--p_147808_3_;
			}

			renderer.aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (renderer.renderMinY <= 0.0D)
			{
				++p_147808_3_;
			}

			i1 = l;

			if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.5F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_, 1))
		{
			if (renderer.renderMaxY >= 1.0D)
			{
				++p_147808_3_;
			}

			renderer.aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (renderer.renderMaxY >= 1.0D)
			{
				--p_147808_3_;
			}

			i1 = l;

			if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_;
			renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_;
			renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_;
			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 1));
			flag = true;
		}

		float f8;
		float f9;
		float f10;
		float f11;
		int j1;
		int k1;
		int l1;
		int i2;
		IIcon iicon;

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1, 2))
		{
			if (renderer.renderMinZ <= 0.0D)
			{
				--p_147808_4_;
			}

			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			renderer.aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (renderer.renderMinZ <= 0.0D)
			{
				++p_147808_4_;
			}

			i1 = l;

			if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f10 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
			f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			f4 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			f6 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 2);
			this.renderFaceZNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceZNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1, 3))
		{
			if (renderer.renderMaxZ >= 1.0D)
			{
				++p_147808_4_;
			}

			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (renderer.renderMaxZ >= 1.0D)
			{
				--p_147808_4_;
			}

			i1 = l;

			if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f10 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			f4 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
			f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			f6 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 3);
			this.renderFaceZPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceZPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_, 4))
		{
			if (renderer.renderMinX <= 0.0D)
			{
				--p_147808_2_;
			}

			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
			}

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
			}

			if (renderer.renderMinX <= 0.0D)
			{
				++p_147808_2_;
			}

			i1 = l;

			if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
			f10 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
			f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
			f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
			f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
			f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
			renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 4);
			this.renderFaceXNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceXNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_, 5))
		{
			if (renderer.renderMaxX >= 1.0D)
			{
				++p_147808_2_;
			}

			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
			}

			if (renderer.renderMaxX >= 1.0D)
			{
				--p_147808_2_;
			}

			i1 = l;

			if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
			f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
			f10 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
			f11 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f3 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * renderer.renderMaxZ);
			f4 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * renderer.renderMinZ);
			f5 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * renderer.renderMinZ);
			f6 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * renderer.renderMaxZ);
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 5);
			this.renderFaceXPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceXPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		renderer.enableAO = false;
		return flag;
	}

	/**
	 * Renders a standard cube block at the given coordinates, with a given color ratio. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, r, g, b
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithColorMultiplier(RenderBlocks renderer, Block p_147736_1_, int p_147736_2_, int p_147736_3_, int p_147736_4_, float p_147736_5_, float p_147736_6_, float p_147736_7_)
	{
		renderer.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f4 * p_147736_5_;
		float f8 = f4 * p_147736_6_;
		float f9 = f4 * p_147736_7_;
		float f10 = f3;
		float f11 = f5;
		float f12 = f6;
		float f13 = f3;
		float f14 = f5;
		float f15 = f6;
		float f16 = f3;
		float f17 = f5;
		float f18 = f6;

		if (p_147736_1_ != Blocks.grass)
		{
			f10 = f3 * p_147736_5_;
			f11 = f5 * p_147736_5_;
			f12 = f6 * p_147736_5_;
			f13 = f3 * p_147736_6_;
			f14 = f5 * p_147736_6_;
			f15 = f6 * p_147736_6_;
			f16 = f3 * p_147736_7_;
			f17 = f5 * p_147736_7_;
			f18 = f6 * p_147736_7_;
		}

		int l = p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_);

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_, 0))
		{
			tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_));
			tessellator.setColorOpaque_F(f10, f13, f16);
			this.renderFaceYNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_, 1))
		{
			tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_));
			tessellator.setColorOpaque_F(f7, f8, f9);
			this.renderFaceYPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1, 2))
		{
			tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 2);
			this.renderFaceZNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				this.renderFaceZNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1, 3))
		{
			tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 3);
			this.renderFaceZPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				this.renderFaceZPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_, 4))
		{
			tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 4);
			this.renderFaceXNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				this.renderFaceXNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_, 5))
		{
			tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 5);
			this.renderFaceXPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				this.renderFaceXPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}
		return flag;
	}

	// Edited vanilla sources beyond this point(I added metadata to the
	// renderStandardBlock*** methods in order to be able to fix
	// flipped sides(via my determineIfSideShouldBeFlipped() method), and
	// I changed the way the renderFace**** methods interpret the
	// renderer.flipTexture variable(named 'flipTextureHorizontally' below) and
	// added a new variable called 'flipTextureVertically', which does what it says):

	/**
	 * Renders a standard cube block with metadata at the given coordinates (WITH FIXED flipped textures!!!1 by Brian_Entei)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithMetadata(RenderBlocks renderer, int metadata, Block block, int x, int y, int z) {
		int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}
		return Minecraft.isAmbientOcclusionEnabled() && block.getLightValue() == 0 ? (renderer.partialRenderBounds ? this.renderStandardBlockWithMetadataWithAmbientOcclusionPartial(renderer, metadata, block, x, y, z, f, f1, f2) : this.renderStandardBlockWithMetadataWithAmbientOcclusion(renderer, metadata, block, x, y, z, f, f1, f2)) : this.renderStandardBlockWithMetadataWithColorMultiplier(renderer, metadata, block, x, y, z, f, f1, f2);
	}

	/**
	 * Renders a standard cube block with ambient occlusion. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, red, green, blue (lighting)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithMetadataWithAmbientOcclusion(RenderBlocks renderer, int metadata, Block p_147751_1_, int p_147751_2_, int p_147751_3_, int p_147751_4_, float p_147751_5_, float p_147751_6_, float p_147751_7_) {
		renderer.enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);
		
		if (renderer.getBlockIcon(p_147751_1_).getIconName().equals("grass_top"))
		{
			flag1 = false;
		}
		else if (renderer.hasOverrideBlockTexture())
		{
			flag1 = false;
		}
		
		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;
		
		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_, 0))//BOTTOM
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 0);
			if (renderer.renderMinY <= 0.0D)
			{
				--p_147751_3_;
			}
			
			renderer.aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();
			
			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (renderer.renderMinY <= 0.0D)
			{
				++p_147751_3_;
			}

			i1 = l;

			if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.5F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_, 1))//TOP
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 1);
			if (renderer.renderMaxY >= 1.0D)
			{
				++p_147751_3_;
			}

			renderer.aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
			}

			if (renderer.renderMaxY >= 1.0D)
			{
				--p_147751_3_;
			}

			i1 = l;

			if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_;
			renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_;
			renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_;
			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1, 2))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 2);
			if (renderer.renderMinZ <= 0.0D)
			{
				--p_147751_4_;
			}

			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			renderer.aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (renderer.renderMinZ <= 0.0D)
			{
				++p_147751_4_;
			}

			i1 = l;

			if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
			f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 2);
			this.renderFaceZNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceZNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1, 3))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 3);
			if (renderer.renderMaxZ >= 1.0D)
			{
				++p_147751_4_;
			}

			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			renderer.aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
			}

			if (renderer.renderMaxZ >= 1.0D)
			{
				--p_147751_4_;
			}

			i1 = l;

			if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
			f6 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f5 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3);
			this.renderFaceZPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3));

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceZPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_, 4))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 4);
			if (renderer.renderMinX <= 0.0D)
			{
				--p_147751_2_;
			}

			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
			}

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
			}

			if (renderer.renderMinX <= 0.0D)
			{
				++p_147751_2_;
			}

			i1 = l;

			if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
			f3 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 4);
			this.renderFaceXNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceXNeg(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147751_1_.shouldSideBeRendered(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_, 5))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 5);
			if (renderer.renderMaxX >= 1.0D)
			{
				++p_147751_2_;
			}

			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			renderer.aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			renderer.aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			renderer.aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			flag2 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
			}

			if (renderer.renderMaxX >= 1.0D)
			{
				--p_147751_2_;
			}

			i1 = l;

			if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).isOpaqueCube())
			{
				i1 = p_147751_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
			f5 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
			f6 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147751_1_, renderer.blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 5);
			this.renderFaceXPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147751_5_;
				renderer.colorRedBottomLeft *= p_147751_5_;
				renderer.colorRedBottomRight *= p_147751_5_;
				renderer.colorRedTopRight *= p_147751_5_;
				renderer.colorGreenTopLeft *= p_147751_6_;
				renderer.colorGreenBottomLeft *= p_147751_6_;
				renderer.colorGreenBottomRight *= p_147751_6_;
				renderer.colorGreenTopRight *= p_147751_6_;
				renderer.colorBlueTopLeft *= p_147751_7_;
				renderer.colorBlueBottomLeft *= p_147751_7_;
				renderer.colorBlueBottomRight *= p_147751_7_;
				renderer.colorBlueTopRight *= p_147751_7_;
				this.renderFaceXPos(renderer, p_147751_1_, (double)p_147751_2_, (double)p_147751_3_, (double)p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		renderer.enableAO = false;
		return flag;
	}

	/**
	 * Renders non-full-cube block with ambient occlusion. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, red, green, blue (lighting)
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithMetadataWithAmbientOcclusionPartial(RenderBlocks renderer, int metadata, Block p_147808_1_, int p_147808_2_, int p_147808_3_, int p_147808_4_, float p_147808_5_, float p_147808_6_, float p_147808_7_)
	{
		renderer.enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);

		if (renderer.getBlockIcon(p_147808_1_).getIconName().equals("grass_top"))
		{
			flag1 = false;
		}
		else if (renderer.hasOverrideBlockTexture())
		{
			flag1 = false;
		}

		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_, 0))//BOTTOM
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 0);
			if (renderer.renderMinY <= 0.0D)
			{
				--p_147808_3_;
			}

			renderer.aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (renderer.renderMinY <= 0.0D)
			{
				++p_147808_3_;
			}

			i1 = l;

			if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.5F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_, 1))//TOP
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 1);
			if (renderer.renderMaxY >= 1.0D)
			{
				++p_147808_3_;
			}

			renderer.aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
			}

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
			}

			if (renderer.renderMaxY >= 1.0D)
			{
				--p_147808_3_;
			}

			i1 = l;

			if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
			renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
			renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
			renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_;
			renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_;
			renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_;
			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			this.renderFaceYPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 1));
			flag = true;
		}

		float f8;
		float f9;
		float f10;
		float f11;
		int j1;
		int k1;
		int l1;
		int i2;
		IIcon iicon;

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1, 2))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 2);
			if (renderer.renderMinZ <= 0.0D)
			{
				--p_147808_4_;
			}

			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			renderer.aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (renderer.renderMinZ <= 0.0D)
			{
				++p_147808_4_;
			}

			i1 = l;

			if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
			f10 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
			f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			f4 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			f6 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 2);
			this.renderFaceZNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceZNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1, 3))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 3);
			if (renderer.renderMaxZ >= 1.0D)
			{
				++p_147808_4_;
			}

			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			renderer.aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
			}

			if (renderer.renderMaxZ >= 1.0D)
			{
				--p_147808_4_;
			}

			i1 = l;

			if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f10 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
			f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
			f4 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
			f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
			f6 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.8F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 3);
			this.renderFaceZPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceZPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_, 4))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 4);
			if (renderer.renderMinX <= 0.0D)
			{
				--p_147808_2_;
			}

			renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
			}

			if (!flag5 && !flag3)
			{
				renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
			}

			if (!flag4 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
				renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
			}

			if (!flag5 && !flag2)
			{
				renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
				renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
			}
			else
			{
				renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
			}

			if (renderer.renderMinX <= 0.0D)
			{
				++p_147808_2_;
			}

			i1 = l;

			if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
			f9 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
			f10 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
			f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
			f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
			f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
			f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
			f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
			renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 4);
			this.renderFaceXNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceXNeg(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147808_1_.shouldSideBeRendered(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_, 5))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 5);
			if (renderer.renderMaxX >= 1.0D)
			{
				++p_147808_2_;
			}

			renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			renderer.aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			renderer.aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			renderer.aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			renderer.aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			flag2 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
			}

			if (!flag3 && !flag4)
			{
				renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
			}

			if (!flag2 && !flag5)
			{
				renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
				renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
			}

			if (!flag2 && !flag4)
			{
				renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
				renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
			}
			else
			{
				renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getAmbientOcclusionLightValue();
				renderer.aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
			}

			if (renderer.renderMaxX >= 1.0D)
			{
				--p_147808_2_;
			}

			i1 = l;

			if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).isOpaqueCube())
			{
				i1 = p_147808_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			}

			f7 = renderer.blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
			f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
			f10 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
			f11 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
			f3 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * renderer.renderMaxZ);
			f4 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * renderer.renderMinZ);
			f5 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * renderer.renderMinZ);
			f6 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * renderer.renderMaxZ);
			j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
			k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
			l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
			i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
			renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
			renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
			renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
			renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

			if (flag1)
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147808_5_ * 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147808_6_ * 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147808_7_ * 0.6F;
			}
			else
			{
				renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
				renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
				renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
			}

			renderer.colorRedTopLeft *= f3;
			renderer.colorGreenTopLeft *= f3;
			renderer.colorBlueTopLeft *= f3;
			renderer.colorRedBottomLeft *= f4;
			renderer.colorGreenBottomLeft *= f4;
			renderer.colorBlueBottomLeft *= f4;
			renderer.colorRedBottomRight *= f5;
			renderer.colorGreenBottomRight *= f5;
			renderer.colorBlueBottomRight *= f5;
			renderer.colorRedTopRight *= f6;
			renderer.colorGreenTopRight *= f6;
			renderer.colorBlueTopRight *= f6;
			iicon = renderer.getBlockIcon(p_147808_1_, renderer.blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 5);
			this.renderFaceXPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				renderer.colorRedTopLeft *= p_147808_5_;
				renderer.colorRedBottomLeft *= p_147808_5_;
				renderer.colorRedBottomRight *= p_147808_5_;
				renderer.colorRedTopRight *= p_147808_5_;
				renderer.colorGreenTopLeft *= p_147808_6_;
				renderer.colorGreenBottomLeft *= p_147808_6_;
				renderer.colorGreenBottomRight *= p_147808_6_;
				renderer.colorGreenTopRight *= p_147808_6_;
				renderer.colorBlueTopLeft *= p_147808_7_;
				renderer.colorBlueBottomLeft *= p_147808_7_;
				renderer.colorBlueBottomRight *= p_147808_7_;
				renderer.colorBlueTopRight *= p_147808_7_;
				this.renderFaceXPos(renderer, p_147808_1_, (double)p_147808_2_, (double)p_147808_3_, (double)p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		renderer.enableAO = false;
		return flag;
	}

	/**
	 * Renders a standard cube block at the given coordinates, with a given color ratio. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, r, g, b
	 */
	@SuppressWarnings("cast")
	public boolean renderStandardBlockWithMetadataWithColorMultiplier(RenderBlocks renderer, int metadata, Block p_147736_1_, int p_147736_2_, int p_147736_3_, int p_147736_4_, float p_147736_5_, float p_147736_6_, float p_147736_7_)
	{
		renderer.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f4 * p_147736_5_;
		float f8 = f4 * p_147736_6_;
		float f9 = f4 * p_147736_7_;
		float f10 = f3;
		float f11 = f5;
		float f12 = f6;
		float f13 = f3;
		float f14 = f5;
		float f15 = f6;
		float f16 = f3;
		float f17 = f5;
		float f18 = f6;

		if (p_147736_1_ != Blocks.grass)
		{
			f10 = f3 * p_147736_5_;
			f11 = f5 * p_147736_5_;
			f12 = f6 * p_147736_5_;
			f13 = f3 * p_147736_6_;
			f14 = f5 * p_147736_6_;
			f15 = f6 * p_147736_6_;
			f16 = f3 * p_147736_7_;
			f17 = f5 * p_147736_7_;
			f18 = f6 * p_147736_7_;
		}

		int l = p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_);

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_, 0))//BOTTOM
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 0);
			tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_));
			tessellator.setColorOpaque_F(f10, f13, f16);
			this.renderFaceYNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 0));
			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_, 1))//TOP
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 1);
			tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_));
			tessellator.setColorOpaque_F(f7, f8, f9);
			this.renderFaceYPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1, 2))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 2);
			tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 2);
			this.renderFaceZNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				this.renderFaceZNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1, 3))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 3);
			tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 3);
			this.renderFaceZPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				this.renderFaceZPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_, 4))//NORMAL SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 4);
			tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 4);
			this.renderFaceXNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				this.renderFaceXNeg(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderer.renderAllFaces || p_147736_1_.shouldSideBeRendered(renderer.blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_, 5))//AFFECTED SIDE
		{
			this.determineIfSideShouldBeFlipped(renderer, metadata, 5);
			tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : p_147736_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = renderer.getBlockIcon(p_147736_1_, renderer.blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 5);
			this.renderFaceXPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, iicon);

			if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				this.renderFaceXPos(renderer, p_147736_1_, (double)p_147736_2_, (double)p_147736_3_, (double)p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}
		return flag;
	}

	/**
	 * Renders the given texture to the bottom face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceYNeg(RenderBlocks renderer, Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_) {
		Tessellator tessellator = Tessellator.instance;

		if(renderer.hasOverrideBlockTexture()) {
			p_147768_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147768_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
		double d4 = (double)p_147768_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);
		double d5 = (double)p_147768_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
		double d6 = (double)p_147768_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if(renderer.flipTexture) {
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if(renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D) {
			d3 = (double)p_147768_8_.getMinU();
			d4 = (double)p_147768_8_.getMaxU();
		}
		if(renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D) {
			d5 = (double)p_147768_8_.getMinV();
			d6 = (double)p_147768_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;

		double d9 = d5;
		double d10 = d6;

		if(renderer.uvRotateBottom == 2) {
			d3 = (double)p_147768_8_.getInterpolatedU(renderer.renderMinZ * 16.0D);
			d5 = (double)p_147768_8_.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
			d4 = (double)p_147768_8_.getInterpolatedU(renderer.renderMaxZ * 16.0D);
			d6 = (double)p_147768_8_.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(renderer.uvRotateBottom == 1) {
			d3 = (double)p_147768_8_.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
			d5 = (double)p_147768_8_.getInterpolatedV(renderer.renderMinX * 16.0D);
			d4 = (double)p_147768_8_.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
			d6 = (double)p_147768_8_.getInterpolatedV(renderer.renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(renderer.uvRotateBottom == 3) {
			d3 = (double)p_147768_8_.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
			d4 = (double)p_147768_8_.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
			d5 = (double)p_147768_8_.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
			d6 = (double)p_147768_8_.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147768_2_ + renderer.renderMinX;
		double d12 = p_147768_2_ + renderer.renderMaxX;
		double d13 = p_147768_4_ + renderer.renderMinY;
		double d14 = p_147768_6_ + renderer.renderMinZ;
		double d15 = p_147768_6_ + renderer.renderMaxZ;

		if(renderer.renderFromInside) {
			d11 = p_147768_2_ + renderer.renderMaxX;
			d12 = p_147768_2_ + renderer.renderMinX;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if(renderer.enableAO) {
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);//x- z+ corner - Top    Left
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);//x- z- corner  - Bottom Left
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);//x+ z- corner  - Bottom Right
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);//x+ z+ corner  - Top    Right
		}
	}

	/**
	 * Renders the given texture to the top face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceYPos(RenderBlocks renderer, Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_)
	{
		Tessellator tessellator = Tessellator.instance;

		if (renderer.hasOverrideBlockTexture())
		{
			p_147806_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147806_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
		double d4 = (double)p_147806_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);
		double d5 = (double)p_147806_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
		double d6 = (double)p_147806_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if (renderer.flipTexture)
		{
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if (renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D)
		{
			d3 = (double)p_147806_8_.getMinU();
			d4 = (double)p_147806_8_.getMaxU();
		}

		if (renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D)
		{
			d5 = (double)p_147806_8_.getMinV();
			d6 = (double)p_147806_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (renderer.uvRotateTop == 1)
		{
			d3 = (double)p_147806_8_.getInterpolatedU(renderer.renderMinZ * 16.0D);
			d5 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
			d4 = (double)p_147806_8_.getInterpolatedU(renderer.renderMaxZ * 16.0D);
			d6 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		}
		else if (renderer.uvRotateTop == 2)
		{
			d3 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
			d5 = (double)p_147806_8_.getInterpolatedV(renderer.renderMinX * 16.0D);
			d4 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
			d6 = (double)p_147806_8_.getInterpolatedV(renderer.renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		}
		else if (renderer.uvRotateTop == 3)
		{
			d3 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
			d4 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
			d5 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
			d6 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147806_2_ + renderer.renderMinX;
		double d12 = p_147806_2_ + renderer.renderMaxX;
		double d13 = p_147806_4_ + renderer.renderMaxY;
		double d14 = p_147806_6_ + renderer.renderMinZ;
		double d15 = p_147806_6_ + renderer.renderMaxZ;

		if (renderer.renderFromInside)
		{
			d11 = p_147806_2_ + renderer.renderMaxX;
			d12 = p_147806_2_ + renderer.renderMinX;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if (renderer.enableAO)
		{
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		}
		else
		{
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		}
	}

	/**
	 * Renders the given texture to the north (z-negative) face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceZNeg(RenderBlocks renderer, Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, IIcon p_147761_8_)
	{
		Tessellator tessellator = Tessellator.instance;

		if (renderer.hasOverrideBlockTexture())
		{
			p_147761_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147761_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
		double d4 = (double)p_147761_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);

		if (renderer.field_152631_f)
		{
			d4 = (double)p_147761_8_.getInterpolatedU((1.0D - renderer.renderMinX) * 16.0D);
			d3 = (double)p_147761_8_.getInterpolatedU((1.0D - renderer.renderMaxX) * 16.0D);
		}

		double d5 = (double)p_147761_8_.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
		double d6 = (double)p_147761_8_.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if (renderer.flipTexture)
		{
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if (renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D)
		{
			d3 = (double)p_147761_8_.getMinU();
			d4 = (double)p_147761_8_.getMaxU();
		}

		if (renderer.renderMinY < 0.0D || renderer.renderMaxY > 1.0D)
		{
			d5 = (double)p_147761_8_.getMinV();
			d6 = (double)p_147761_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (renderer.uvRotateEast == 2)
		{
			d3 = (double)p_147761_8_.getInterpolatedU(renderer.renderMinY * 16.0D);
			d4 = (double)p_147761_8_.getInterpolatedU(renderer.renderMaxY * 16.0D);
			d5 = (double)p_147761_8_.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
			d6 = (double)p_147761_8_.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		}
		else if (renderer.uvRotateEast == 1)
		{
			d3 = (double)p_147761_8_.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
			d4 = (double)p_147761_8_.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
			d5 = (double)p_147761_8_.getInterpolatedV(renderer.renderMaxX * 16.0D);
			d6 = (double)p_147761_8_.getInterpolatedV(renderer.renderMinX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		}
		else if (renderer.uvRotateEast == 3)
		{
			d3 = (double)p_147761_8_.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
			d4 = (double)p_147761_8_.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
			d5 = (double)p_147761_8_.getInterpolatedV(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147761_8_.getInterpolatedV(renderer.renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147761_2_ + renderer.renderMinX;
		double d12 = p_147761_2_ + renderer.renderMaxX;
		double d13 = p_147761_4_ + renderer.renderMinY;
		double d14 = p_147761_4_ + renderer.renderMaxY;
		double d15 = p_147761_6_ + renderer.renderMinZ;

		if (renderer.renderFromInside)
		{
			d11 = p_147761_2_ + renderer.renderMaxX;
			d12 = p_147761_2_ + renderer.renderMinX;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if (renderer.enableAO)
		{
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		}
		else
		{
			tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		}
	}

	/**
	 * Renders the given texture to the south (z-positive) face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceZPos(RenderBlocks renderer, Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_)
	{
		Tessellator tessellator = Tessellator.instance;

		if (renderer.hasOverrideBlockTexture())
		{
			p_147734_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147734_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
		double d4 = (double)p_147734_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);
		double d5 = (double)p_147734_8_.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
		double d6 = (double)p_147734_8_.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if (renderer.flipTexture)
		{
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if (renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D)
		{
			d3 = (double)p_147734_8_.getMinU();
			d4 = (double)p_147734_8_.getMaxU();
		}

		if (renderer.renderMinY < 0.0D || renderer.renderMaxY > 1.0D)
		{
			d5 = (double)p_147734_8_.getMinV();
			d6 = (double)p_147734_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (renderer.uvRotateWest == 1)
		{
			d3 = (double)p_147734_8_.getInterpolatedU(renderer.renderMinY * 16.0D);
			d6 = (double)p_147734_8_.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
			d4 = (double)p_147734_8_.getInterpolatedU(renderer.renderMaxY * 16.0D);
			d5 = (double)p_147734_8_.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		}
		else if (renderer.uvRotateWest == 2)
		{
			d3 = (double)p_147734_8_.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
			d5 = (double)p_147734_8_.getInterpolatedV(renderer.renderMinX * 16.0D);
			d4 = (double)p_147734_8_.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
			d6 = (double)p_147734_8_.getInterpolatedV(renderer.renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		}
		else if (renderer.uvRotateWest == 3)
		{
			d3 = (double)p_147734_8_.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
			d4 = (double)p_147734_8_.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
			d5 = (double)p_147734_8_.getInterpolatedV(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147734_8_.getInterpolatedV(renderer.renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147734_2_ + renderer.renderMinX;
		double d12 = p_147734_2_ + renderer.renderMaxX;
		double d13 = p_147734_4_ + renderer.renderMinY;
		double d14 = p_147734_4_ + renderer.renderMaxY;
		double d15 = p_147734_6_ + renderer.renderMaxZ;

		if (renderer.renderFromInside)
		{
			d11 = p_147734_2_ + renderer.renderMaxX;
			d12 = p_147734_2_ + renderer.renderMinX;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if (renderer.enableAO)
		{
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
		}
		else
		{
			tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
		}
	}

	/**
	 * Renders the given texture to the west (x-negative) face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceXNeg(RenderBlocks renderer, Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_)
	{
		Tessellator tessellator = Tessellator.instance;

		if (renderer.hasOverrideBlockTexture())
		{
			p_147798_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147798_8_.getInterpolatedU(renderer.renderMinZ * 16.0D);
		double d4 = (double)p_147798_8_.getInterpolatedU(renderer.renderMaxZ * 16.0D);
		double d5 = (double)p_147798_8_.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
		double d6 = (double)p_147798_8_.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if(renderer.flipTexture)
		{
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if (renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D)
		{
			d3 = (double)p_147798_8_.getMinU();
			d4 = (double)p_147798_8_.getMaxU();
		}

		if (renderer.renderMinY < 0.0D || renderer.renderMaxY > 1.0D)
		{
			d5 = (double)p_147798_8_.getMinV();
			d6 = (double)p_147798_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (renderer.uvRotateNorth == 1)
		{
			d3 = (double)p_147798_8_.getInterpolatedU(renderer.renderMinY * 16.0D);
			d5 = (double)p_147798_8_.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
			d4 = (double)p_147798_8_.getInterpolatedU(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147798_8_.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		}
		else if (renderer.uvRotateNorth == 2)
		{
			d3 = (double)p_147798_8_.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
			d5 = (double)p_147798_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
			d4 = (double)p_147798_8_.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
			d6 = (double)p_147798_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		}
		else if (renderer.uvRotateNorth == 3)
		{
			d3 = (double)p_147798_8_.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
			d4 = (double)p_147798_8_.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
			d5 = (double)p_147798_8_.getInterpolatedV(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147798_8_.getInterpolatedV(renderer.renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147798_2_ + renderer.renderMinX;
		double d12 = p_147798_4_ + renderer.renderMinY;
		double d13 = p_147798_4_ + renderer.renderMaxY;
		double d14 = p_147798_6_ + renderer.renderMinZ;
		double d15 = p_147798_6_ + renderer.renderMaxZ;

		if (renderer.renderFromInside)
		{
			d14 = p_147798_6_ + renderer.renderMaxZ;
			d15 = p_147798_6_ + renderer.renderMinZ;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if (renderer.enableAO)
		{
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
		}
		else
		{
			tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
			tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
		}
	}

	/**
	 * Renders the given texture to the east (x-positive) face of the block. (WITH FIXED flipped textures!!!1 by Brian_Entei) Args: block, x, y, z, texture
	 */
	@SuppressWarnings("cast")
	public void renderFaceXPos(RenderBlocks renderer, Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_)
	{
		Tessellator tessellator = Tessellator.instance;

		if (renderer.hasOverrideBlockTexture())
		{
			p_147764_8_ = renderer.overrideBlockTexture;
		}

		double d3 = (double)p_147764_8_.getInterpolatedU(renderer.renderMinZ * 16.0D);
		double d4 = (double)p_147764_8_.getInterpolatedU(renderer.renderMaxZ * 16.0D);

		if (renderer.field_152631_f)
		{
			d4 = (double)p_147764_8_.getInterpolatedU((1.0D - renderer.renderMinZ) * 16.0D);
			d3 = (double)p_147764_8_.getInterpolatedU((1.0D - renderer.renderMaxZ) * 16.0D);
		}

		double d5 = (double)p_147764_8_.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
		double d6 = (double)p_147764_8_.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);
		double d7;

		//Old vanilla code that is not really in the right spot:
		/*if(renderer.flipTexture)
		{
			d7 = d3;
			d3 = d4;
			d4 = d3;
		}*/

		if (renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D)
		{
			d3 = (double)p_147764_8_.getMinU();
			d4 = (double)p_147764_8_.getMaxU();
			
		}

		if (renderer.renderMinY < 0.0D || renderer.renderMaxY > 1.0D)
		{
			d5 = (double)p_147764_8_.getMinV();
			d6 = (double)p_147764_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (renderer.uvRotateSouth == 2)
		{
			d3 = (double)p_147764_8_.getInterpolatedU(renderer.renderMinY * 16.0D);
			d5 = (double)p_147764_8_.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
			d4 = (double)p_147764_8_.getInterpolatedU(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147764_8_.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		}
		else if (renderer.uvRotateSouth == 1)
		{
			d3 = (double)p_147764_8_.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
			d5 = (double)p_147764_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);
			d4 = (double)p_147764_8_.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
			d6 = (double)p_147764_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		}
		else if (renderer.uvRotateSouth == 3)
		{
			d3 = (double)p_147764_8_.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
			d4 = (double)p_147764_8_.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
			d5 = (double)p_147764_8_.getInterpolatedV(renderer.renderMaxY * 16.0D);
			d6 = (double)p_147764_8_.getInterpolatedV(renderer.renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147764_2_ + renderer.renderMaxX;
		double d12 = p_147764_4_ + renderer.renderMinY;
		double d13 = p_147764_4_ + renderer.renderMaxY;
		double d14 = p_147764_6_ + renderer.renderMinZ;
		double d15 = p_147764_6_ + renderer.renderMaxZ;

		if (renderer.renderFromInside)
		{
			d14 = p_147764_6_ + renderer.renderMaxZ;
			d15 = p_147764_6_ + renderer.renderMinZ;
		}

		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureHorizontally) {//swap d3 and d4, then swap d7 and d8
			double placeholder = d3;
			d3 = d4;
			d4 = placeholder;
			
			placeholder = d7;
			d7 = d8;
			d8 = placeholder;
		}
		//New and improved code by me(Brian_Entei)! This even lets you flip textures vertically too!
		if(this.flipTextureVertically) {//swap d5 and d6, then swap d9 and d10
			double placeholder = d5;
			d5 = d6;
			d6 = placeholder;
			
			placeholder = d9;
			d9 = d10;
			d10 = placeholder;
		}

		if (renderer.enableAO)
		{
			tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
			tessellator.setBrightness(renderer.brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
			tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
			tessellator.setBrightness(renderer.brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
			tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
			tessellator.setBrightness(renderer.brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
			tessellator.setBrightness(renderer.brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
		}
		else
		{
			tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
			tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
		}
	}

}
