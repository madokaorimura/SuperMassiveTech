package tterrag.supermassivetech.client.config;

import static tterrag.supermassivetech.common.config.ConfigHandler.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

@SuppressWarnings("rawtypes")
public class ConfigGuiSMT extends GuiConfig
{
    public ConfigGuiSMT(GuiScreen parentScreen)
    {
        super(parentScreen, getConfigElements(parentScreen), ModProps.MODID, false, false, Utils.lang.localize("config.title"));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent)
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = ModProps.LOCALIZING + ".config.";

        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionArmor.toLowerCase()).setLanguageKey(prefix + sectionArmor.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionEnchants.toLowerCase()).setLanguageKey(prefix + sectionEnchants.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionGravity.toLowerCase()).setLanguageKey(prefix + sectionGravity.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionTooltips.toLowerCase()).setLanguageKey(prefix + sectionTooltips.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionMisc.toLowerCase()).setLanguageKey(prefix + sectionMisc.toLowerCase().replace(" ", ""))));

        return list;
    }
}