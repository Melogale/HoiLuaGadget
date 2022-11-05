package sg;

public class Paths {

    public static final boolean laptop = false;

    public static final String HOI_PATH =
            laptop ?
                    "C:/Program Files (x86)/Steam/steamapps/common/Hearts of Iron IV" :
                    "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";
    public static final String DIV_PATH =
            laptop ?
                    "C:/Users/zacha/Documents/Paradox Interactive/Hearts of Iron IV/mod/Divergences" :
                    "E:/Libraries/Documents/Paradox Interactive/Hearts of Iron IV/mod/Divergences";

    public static final String HOI_STATES = HOI_PATH + "/history/states";
    public static final String DIV_STATES = DIV_PATH + "/history/states";

    public static final String HOI_STRATS = HOI_PATH + "/map/strategicregions";
    public static final String DIV_STRATS = DIV_PATH + "/map/strategicregions";

    public static final String DIV_PROVINCES_BMP = DIV_PATH + "/map/provinces.bmp";

    public static final String HOI_DEFINITIONS_CSV = HOI_PATH + "/map/definition.csv";

    public static final String DESKTOP_FOLDER = "E:/Libraries/Desktop/hoi";

}
