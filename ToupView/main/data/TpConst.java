package com.example.astroclient;

/**
 * Created by hyayh on 2017/11/29.
 */

public class TpConst {
	private static final int CONST = 0x0547;
	private static final int MSG_CONST = CONST + 0x21;
	private static final int RET_CONST = CONST + 0x51;
	public static final int MSG_WB_TEMP = MSG_CONST;
	public static final int MSG_WB_TINT = MSG_CONST + 1;
	public static final int MSG_EXP_TGT = MSG_CONST + 2;
	public static final int MSG_EXP_TIME = MSG_CONST + 3;
	public static final int MSG_EXP_GAIN = MSG_CONST + 4;
	public static final int MSG_HUE = MSG_CONST + 5;
	public static final int MSG_SATURATION = MSG_CONST + 6;
	public static final int MSG_BRIGHTNESS = MSG_CONST + 7;
	public static final int MSG_CONTRAST = MSG_CONST + 8;
	public static final int MSG_GAMMA = MSG_CONST + 9;
	public static final int MSG_DFT_EXP = MSG_CONST + 10;
	public static final int MSG_DFT_WB = MSG_CONST + 11;
	public static final int MSG_DFT_COLOR = MSG_CONST + 12;
	public static final int MSG_AWB = MSG_CONST + 13;
	public static final int MSG_AEXP = MSG_CONST + 14;
	public static final int MSG_AC60 = MSG_CONST + 15;
	public static final int MSG_AC50 = MSG_CONST + 16;
	public static final int MSG_DC = MSG_CONST + 17;
	public static final int MSG_BIN = MSG_CONST + 18;
	public static final int MSG_SKIP = MSG_CONST + 19;
	public static final int MSG_LLC = MSG_CONST + 20;
	public static final int MSG_SHARPNESS = MSG_CONST + 21;
	public static final int MSG_DENOISE = MSG_CONST + 22;
	public static final int MSG_WB_RED = MSG_CONST + 23;
	public static final int MSG_WB_GREEN = MSG_CONST + 24;
	public static final int MSG_WB_BLUE = MSG_CONST + 25;
	public static final int MSG_UPDATE = MSG_CONST + 26;
	public static final int MSG_ENUM = MSG_CONST + 27;
	public static final int MSG_PREVIEW = MSG_CONST + 28;
	public static final int MSG_HFLIP = MSG_CONST + 29;
	public static final int MSG_VFLIP = MSG_CONST + 30;
	public static final int MSG_PARAM = MSG_CONST + 31;
	public static final int MSG_ERROR = MSG_CONST + 32;
	public static final int MSG_RESOLUTION = MSG_CONST + 33;
	public static final int MSG_DEVICE_CHANGED = MSG_CONST + 34;
	public static final int MSG_CHART_UPDATE = MSG_CONST + 35;
	public static final int MSG_VIDEOSIZE_CHANGED = RET_CONST + 2;
	public static final int MSG_STARTRECORD = RET_CONST + 6;
	public static final int MSG_REC_SUCCESS = RET_CONST + 7;
	public static final int MSG_REC_FAILED = RET_CONST + 8;
	public static final int MSG_CAP_SUCCESS = RET_CONST + 9;
	public static final int MSG_CAP_FAILED = RET_CONST + 10;
    public static final int MSG_CAP_PROCESSED = RET_CONST + 11;
	public static final int REQUEST_FILEVIEW = 0x01;
	public static final int DFT_MAX_PROGRESS = 1000;
	public static final String PREF_CONFIG = "Config_Info";
	public static final String PREF_PREFIX = "Config_Prefx";
	public static final String PREF_IMGTYPE = "Config_ImgType";
	public static final String PREF_IMGFMT = "Config_ImgFmt";
	public static final String PREF_IMGDIR = "Config_ImgDir";
	public static final String PREF_FR = "Config_fe";
	public static final String PREF_SAVEINSD = "Config_SaveLoc";
	public static final String PREF_SAVEWITHLAYER = "Config_SaveWithLayer";
	public static final String PREF_SAVEWITHBURN = "Config_SaveWithBurn";
	public static final String PREF_CALIRATE = "Config_CaliRate";
	public static final String PREF_CALIUNIT = "Config_CaliUnit";
	public static final String PREF_CALIINDEX = "Config_CaliIndex";
	public static final String PREF_CAMNAME = "camera_name";
    public static final String PREF_IMAGEINDEX = "image_index";
    public static final String PREF_ROI_WB = "roi_wb";
    public static final String PREF_ROI_AEP = "roi_exp";
	public static final String KEY_ID = "CameraID";
	public static final String STR_DEFAULTID = "CameraDemo";
	public static final String PREF_CALIB = "calibration_items";

	public static final int REQ_PERMISSIONS = 100;
	public static final int REQ_CAMERA = 101;
	public static final int REQ_STORAGE = 102;
	public static final int REQ_LOCATION = 103;


	public enum SHAPE_TYPE {
		TYPE_NONE,
		TYPE_DOT,
		TYPE_LINE,
		TYPE_DOUBLELINE,
		TYPE_ARROWLINE,
		TYPE_CORNER,
		TYPE_RECTANGLE,
		TYPE_POLYGON,
		TYPE_CIRCLE,
		TYPE_TEXT,
		TYPE_SCALEBAR,
		TYPE_CALIBRATION,
		TYPE_FOUCSGRAPHIC;
	}

	public enum GRAPHIC_THICKNESS {
		THICKNESS_S(0),
		THICKNESS_M(1),
		THICKNESS_L(2),
		THICKNESS_XL(3),
		THICKNESS_XXL(4);
		private final int mCode;

		private GRAPHIC_THICKNESS(int code) {
			this.mCode = code;
		}

		public int getCode() {
			return mCode;
		}

		@Override
		public String toString() {
			return String.valueOf(this.mCode);
		}
	}

	public enum GRAPHIC_LINECOLOR {
		COLOR_LINE1(0),
		COLOR_LINE2(1),
		COLOR_LINE3(2),
		COLOR_LINE4(3),
		COLOR_LINE5(4),
		COLOR_LINE6(5),
		COLOR_LINE7(7);
		private final int mCode;

		private GRAPHIC_LINECOLOR(int code) {
			this.mCode = code;
		}

		public int getCode() {
			return mCode;
		}

		@Override
		public String toString() {
			return String.valueOf(this.mCode);
		}
	}

	public enum POINT_STATE {
		STATE_NORMAL,
		STATE_ADD,
		STATE_SUB;
	}

	public enum ePage {
		PAGE_NONE,
		PAGE_CAMLIST,
		PAGE_TOUPVIEW,
		PAGE_BROWSER,
		PAGE_INFO
	}

	public enum eState {
		STATE_UNCERTAIN ,
		STATE_HIDE,
		STATE_SHOW
	}
}
