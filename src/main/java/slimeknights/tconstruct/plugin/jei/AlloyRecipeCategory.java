package slimeknights.tconstruct.plugin.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import slimeknights.tconstruct.library.Util;

public class AlloyRecipeCategory implements IRecipeCategory {

  public static String CATEGORY = Util.prefix("alloy");
  public static ResourceLocation background_loc = Util.getResource("textures/gui/jei/smeltery.png");

  protected final IDrawable background;
  protected final IDrawableAnimated arrow;

  public AlloyRecipeCategory(IGuiHelper guiHelper) {
    background = guiHelper.createDrawable(background_loc, 0, 60, 160, 60);

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_loc, 160, 60, 24, 17);
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
  }

  @Nonnull
  @Override
  public String getUid() {
    return CATEGORY;
  }

  @Nonnull
  @Override
  public String getTitle() {
    return Util.translate("gui.jei.alloy.title");
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public void drawExtras(Minecraft minecraft) {

  }

  @Override
  public void drawAnimations(Minecraft minecraft) {
    arrow.draw(minecraft, 76, 22);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
    if(recipeWrapper instanceof AlloyRecipeWrapper) {
      AlloyRecipeWrapper recipe = (AlloyRecipeWrapper) recipeWrapper;
      IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

      float w = 36f/recipe.getFluidInputs().size();

      // find maximum used amount in the recipe so relations are correct
      int max_amount = 0;
      for(FluidStack fs : recipe.getFluidInputs()) {
        if(fs.amount > max_amount) {
          max_amount = fs.amount;
        }
      }
      for(FluidStack fs : recipe.getFluidOutputs()) {
        if(fs.amount > max_amount) {
          max_amount = fs.amount;
        }
      }

      // inputs
      for(int i = 0; i < recipe.getFluidInputs().size(); i++) {
        FluidStack in = recipe.getFluidInputs().get(i);
        int x = 21 + (int)(i*w);
        int _w = (int)((i+1)*w - i*w);
        fluids.init(i+1, true, x, 11, _w, 32, max_amount, false, null);
        fluids.set(i+1, in);
      }

      // output
      fluids.init(0, false, 118, 11, 18, 32, max_amount, false, null);
      fluids.set(0, recipe.getFluidOutputs());
    }
  }
}
