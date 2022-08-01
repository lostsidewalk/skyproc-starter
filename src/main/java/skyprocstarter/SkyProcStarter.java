package skyprocstarter;

import lev.gui.LSaveFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import skyproc.*;
import skyproc.gui.*;
import skyprocstarter.YourSaveFile.Settings;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"skyproc", "skyprocstarter"},
        // eventually remove SUM from skyproc core module
        excludeFilters = {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=SUMprogram.class)}
)
public abstract class SkyProcStarter implements SUM {

    public static final Color headerColor = new Color(66, 181, 184);  // Teal

    public static final Color settingsColor = new Color(72, 179, 58);  // Green

    @Autowired
    Function<SPMainMenuPanel, SPSettingPanel> welcomePanelFactory;

    @Autowired
    Function<SPMainMenuPanel, SPSettingPanel> otherSettingsPanelFactory;

    @Autowired
    SPGlobal spGlobal;

    /*
     * The important functions to change are:
     * - getStandardMenu(), where you set up the GUI
     * - runChangesToPatch(), where you put all the processing code and add records to the output patch.
     */

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

    public static Font settingsFont;

    @PostConstruct
    void postConstruct() {
        myPatchName = this.spLocalPatchName;
        welcomeText = this.spLocalWelcomeText;
        settingsFont = new Font(this.fontName, Font.BOLD, 15);
        log.info("SkyProcStarter post-construct");
        SUMGUI.open(spGlobal, this, new String[] {});
    }

    public static final SkyProcSave save = new YourSaveFile();
    /*
     * The types of records you want your patcher to import. Change this to
     * customize the import to what you need.
     */
    final GRUP_TYPE[] importRequests = new GRUP_TYPE[] {
            GRUP_TYPE.INGR,
            GRUP_TYPE.WEAP
    };

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

    // This is where you add panels to the main menu.
    // First create custom panel classes (as shown by OtherSettingsPanel),
    // Then add them here.
    @Override
    public SPMainMenuPanel getStandardMenu() {
        SPMainMenuPanel settingsMenu = new SPMainMenuPanel(getHeaderColor());

        settingsMenu.setWelcomePanel(welcomePanelFactory.apply(settingsMenu));

        settingsMenu.addMenu(otherSettingsPanelFactory.apply(settingsMenu), false, save, Settings.OTHER_SETTINGS);

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
        out.setAuthor(authorName);
        return out;
    }

    @Override
    public Color getHeaderColor() {
        return headerColor;
    }

    // Add any custom checks to determine if a patch is needed.
    // On Automatic Variants, this function would check if any new packages were
    // added or removed.
    @Override
    public boolean needsPatching() {
        return false;
    }

    // This function runs when the program opens to "set things up"
    // It runs right after the save file is loaded, and before the GUI is displayed
    @Override
    public void onStart() {
    }

    // This function runs right as the program is about to close.
    @Override
    public void onExit(boolean patchWasGenerated) {
    }

    // Add any mods that you REQUIRE to be present in order to patch.
    @Override
    public ArrayList<ModListing> requiredMods() {
        return new ArrayList<>(0);
    }

    @Override
    public String description() {
        return descriptionToShowInSUM;
    }

    // This is where you should write the bulk of your code.
    // Write the changes you would like to make to the patch,
    // but DO NOT export it.  Exporting is handled internally.
    @Override
    public void runChangesToPatch() {

        Mod patch = SPGlobal.getGlobalPatch();

        Mod merger = new Mod(getName() + "Merger", false);
        merger.addAsOverrides(SPGlobal.getDB());

        // Write your changes to the patch here.
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
        builder.headless(false);
        @SuppressWarnings("unused") ConfigurableApplicationContext context = builder.run(args);
    }
}
