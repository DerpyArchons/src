package creativetab;

import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.referance.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabArcana {
	public static final CreativeTabs CreativeTabArcana = new CreativeTabs(Reference.MOD_ID)
	   {
	     @Override
     public Item getTabIconItem()
	      {
	           return ModItems.ItemDerrpybook;
	     }

	       @Override
	       public String getTranslatedTabLabel()
        {
	          return "Arcana";
        }
	   };
	}