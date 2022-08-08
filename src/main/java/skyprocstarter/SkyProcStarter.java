package skyprocstarter;

import lev.gui.LSaveFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import skyproc.*;
import skyproc.gui.*;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"skyproc", "skyprocstarter"},
        excludeFilters = {
                // eventually remove SUM from skyproc core module
                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=SUMprogram.class)
        }
)
public abstract class SkyProcStarter implements SUM {

    public static final Color headerColor = new Color(66, 181, 184);  // Teal

    public static final Color settingsColor = new Color(72, 179, 58);  // Green

    @Autowired
    Function<SPMainMenuPanel, SPSettingPanel> welcomePanelFactory;

    static class SettingsPanelDescriptor {
        SPSettingPanel panel;
        boolean checkboxPresent;
        public Enum<?> setting;

        SettingsPanelDescriptor(SPSettingPanel panel, boolean checkboxPresent, Enum<?> setting) {
            this.panel = panel;
            this.checkboxPresent = checkboxPresent;
            this.setting = setting;
        }
    }

    @Autowired
    Function<SPMainMenuPanel, List<SettingsPanelDescriptor>> otherSettingsPanelFactory;

    @Autowired
    Consumer<Mod> customerPatcher;

    @Autowired
    SPGlobal spGlobal;

    @Value("${sp.local.patch.name}") // "My Patch"
    private String spLocalPatchName;
    public static String myPatchName;

    @Value("${sp.local.patch.author}") // "Me"
    public String authorName;

    @Value("${sp.local.patch.version}") // "1.0"
    public String version;

    @Value("${sp.local.patch.welcome}") // "This is the standard starter project for SkyProc. I hope it helps you get on your way to making an awesome patcher!"
    private String spLocalWelcomeText;
    public static String welcomeText;

    @Value("${sp.local.patch.sum.description}") // "A brand new SkyProc patcher. Does lots of stuff."
    public String descriptionToShowInSUM;

    @Value("${sp.local.patch.font.name}")
    public String fontName;

    @Value("${sp.local.patch.is.string.tabled}")
    public boolean isStringTabled;

    /*
     * The types of records you want your patcher to import. Change this to
     * customize the import to what you need.
     */
    @Value("${sp.local.patch.import.requests}")
    GRUP_TYPE[] importRequests;

    @Value("${sp.local.patch.needs.patching}")
    boolean needsPatching;

    @Value("${sp.local.patch.required.mods}")
    String[] requiredMods;

    @Autowired
    ApplicationArguments applicationArguments;

    public static Font settingsFont;

    @PostConstruct
    void postConstruct() {
        myPatchName = this.spLocalPatchName;
        welcomeText = this.spLocalWelcomeText;
        settingsFont = new Font(this.fontName, Font.BOLD, 15);
        log.info("SkyProcStarter post-construct");
        String[] sumGUIArgs = handleArgs(this.applicationArguments.getSourceArgs());
        SUMGUI.open(spGlobal, this, sumGUIArgs);
    }

    static String[] handleArgs(String[] args) {
        ArrayList<String> argsList = new ArrayList<>();

        for (String s : args) {
            argsList.add(s.toUpperCase());
        }

        String[] sumGUIArgs = new String[1];
        if (argsList.contains("-GENPATCH")) {
            sumGUIArgs[0] = "-GENPATCH";
        }

        return sumGUIArgs;
    }

    private static String arrToString(String[] arr) {
        StringBuilder sb = new StringBuilder();
        if (arr != null) {
            for (String s : arr) {
                if (s != null) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(s);
                }
            }
        }

        return sb.toString();
    }

    @Autowired
    SkyProcSave save;

    @Override
    public String getName() {
        return myPatchName;
    }

    // This function labels any record types that you "multiply".
    // For example, if you took all the armors in a mod list and made 3 copies,
    // you would put ARMO here.
    // This is to help monitor/prevent issues where multiple SkyProc patchers
    // multiply the same record type to yeild a huge number of records.
    @Override
    public GRUP_TYPE[] dangerousRecordReport() {
        // None
        return new GRUP_TYPE[0];
    }

    @Override
    public GRUP_TYPE[] importRequests() {
        return importRequests;
    }

    @Override
    public boolean importAtStart() {
        return false;
    }

    @Override
    public boolean hasStandardMenu() {
        return true;
    }

    @Override
    public SPMainMenuPanel getStandardMenu() {
        SPMainMenuPanel settingsMenu = new SPMainMenuPanel(getHeaderColor());

        settingsMenu.setWelcomePanel(welcomePanelFactory.apply(settingsMenu));

        otherSettingsPanelFactory.apply(settingsMenu).forEach(m -> settingsMenu.addMenu(m.panel, m.checkboxPresent, save, m.setting));

        return settingsMenu;
    }

    // Usually false unless you want to make your own GUI
    @Override
    public boolean hasCustomMenu() {
        return false;
    }

    @Override
    public JFrame openCustomMenu() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public boolean hasLogo() {
        return false;
    }

    @Override
    public URL getLogo() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public boolean hasSave() {
        return true;
    }

    @Override
    public LSaveFile getSave() {
        return save;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public ModListing getListing() {
        return new ModListing(getName(), false);
    }

    @Override
    public Mod getExportPatch() {
        Mod out = new Mod(getListing());
        out.setFlag(Mod.Mod_Flags.STRING_TABLED, isStringTabled);
        out.setAuthor(authorName);
        return out;
    }

    @Override
    public Color getHeaderColor() {
        return headerColor;
    }

    @Override
    public boolean needsPatching() {
        return needsPatching;
    }

    // This function runs when the program opens to "set things up"
    // It runs right after the save file is loaded, and before the GUI is displayed
    @Autowired
    Runnable preProcessor;

    @Override
    public void onStart() {
        if (preProcessor != null) {
            preProcessor.run();
        }
    }

    // This function runs right as the program is about to close.
    @Autowired
    Runnable postProcessor;

    @Override
    public void onExit(boolean patchWasGenerated) {
        if (postProcessor != null) {
            postProcessor.run();
        }
    }

    @Override
    public ArrayList<ModListing> requiredMods() {
        if (ArrayUtils.isNotEmpty(requiredMods)) {
            ArrayList<ModListing> mods = new ArrayList<>(requiredMods.length);
            for (String m : requiredMods) {
                mods.add(new ModListing(m));
            }
            return mods;
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public String description() {
        return descriptionToShowInSUM;
    }

    @Override
    public void runChangesToPatch() {

        Mod patch = SPGlobal.getGlobalPatch();

        Mod merger = new Mod(getName() + "Merger", false);
        merger.addAsOverrides(SPGlobal.getDB());

        customerPatcher.accept(patch);
    }

    //
    //
    //

    /**
     * Main function that starts the program and GUI.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SkyProcStarter.class);
        builder.headless(false); // TODO: could inspect args for -GENPATCH
        @SuppressWarnings("unused") ConfigurableApplicationContext context = builder.run(args);
    }
}
