import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.awt.event.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.plaf.metal.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTransferable;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.StandardChartTheme;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.util.CellRangeAddress;

public final class MoneyTree extends ApplicationFrame
  							 implements ActionListener, TreeSelectionListener
{
  private static final Calendar calendar = Calendar.getInstance();
  private static String[]	months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"},
								monthn = {"01","02","03","04","05","06","07","08","09","10","11","12"},
								dayn = {"00", "01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
  private static final String C_YEAR = String.valueOf(calendar.get(Calendar.YEAR));
  private static final int C_YEAR_N = calendar.get(Calendar.YEAR);
  private static final int C_MONTH = calendar.get(Calendar.MONTH);
  private static final int C_DAY = calendar.get(Calendar.DAY_OF_MONTH);
  private static final String EXIT_COMMAND = "EXIT",
							RBI_DB = "RBI",
							MCX_DB = "MCX",
							NCDEX_DB = "NCDEX",
							NSE_DB = "NSE",
							BSE_DB = "BSE",
							NSEFNO_DB = "NSEFNO",
							NSEOPT_DB = "NSEOPT",
							MYSCRIPS_DB = "MYSCRIPS",
							WORLDINDICES_DB = "WORLDINDICES",
							FIIDII_DB = "FIIDII",
							FUNDAMENTAL_DB = "FUNDAMENTAL",
							FROM_DATE = "'2009-02-01'",
							FROM_DATE_FOR_CHARTS = "'2011-12-01'",
							PC_FROM = "'2011-09-01'",
							TO_DATE = "CURDATE()";

  private static String[] RbiTables = new String[5],
  						NseEqTables = new String[5500],
						BseEqTables = new String[8000],
						NseFnoTables = new String[36000],
						NseOptTables = new String[99999],
						McxTables = new String[3000],
						NcdexTables = new String[3000],
						FIIDIITables = new String[3000],
						WorldIndicesTables = new String[40],
						NseFnoFromFile = new String[500],
						mynseequity = new String[500],
						mybseequity = new String[500],
						mynsefno = new String[500],
						mymcx = new String[500],
						myncdex = new String[500],
						dates = new String[4000],
						ohlc = new String[4000],
						closeData = new String[4000],
						tradeDates = new String[4000],
						temp = new String[10],
							//HEROHONDA_eq Last trade date 5 aug 11
							//INFOSYSTCH_eq Last trade date 28 jun 11
							//COALINDIA_eq First trade date  4 nov 10
							nifty50 = {
							"RELIANCE_eq","INFY_eq",
							"ITC_eq","HDFC_eq",
							"ICICIBANK_eq","HDFCBANK_eq",
							"LT_eq","TCS_eq",
							"BHARTIARTL_eq","SBIN_eq",
							//10
							"ONGC_eq","HINDUNILVR_eq",
							"MM_eq","TATAMOTORS_eq",
							"NTPC_eq","TATASTEEL_eq",
							"BAJAJAUTO_eq","BHEL_eq",
							"WIPRO_eq","JINDALSTEL_eq",
							//20
							"HEROMOTOCO_eq","SUNPHARMA_eq",
							"COALINDIA_eq","HINDALCO_eq",
							"CIPLA_eq", "STER_eq",
							"TATAPOWER_eq","MARUTI_eq",
							"DLF_eq","JPASSOCIAT_eq",
							//30
							"AXISBANK_eq","DRREDDY_eq",
							"GAIL_eq","KOTAKBANK_eq",
							"GRASIM_eq","POWERGRID_eq",
							"IDFC_eq","PNB_eq",
							"AMBUJACEM_eq","ACC_eq",
							//40
							"HCLTECH_eq","CAIRN_eq",
							"BPCL_eq","RANBAXY_eq",
							"SESAGOA_eq","SIEMENS_eq",
							"SAIL_eq","RELINFRA_eq",
							"RCOM_eq","RPOWER_eq",
							"RELCAPITAL_eq"},
						actualNseFno,
							nonNifty = {
							"ABAN_eq",
							"ABB_eq",
							"ABGSHIP_eq",
							"ABIRLANUVO_eq",
							"ADANIENT_eq",
							"ADANIPORTS_eq",
							"ADANIPOWER_eq",
							"ALBK_eq",
							"ALOKTEXT_eq",
							"ALSTOMTD_eq",
							"ANDHRABANK_eq",
							"APIL_eq",
							"APOLLOTYRE_eq",
							"ARVIND_eq",
							"ASHOKLEY_eq",
							"ASIANPAINT_eq",
							"AUROPHARMA_eq",
							"BAJAJHIND_eq",
							"BAJAJHLDNG_eq",
							"BALRAMCHIN_eq",
							"BANKBARODA_eq",
							"BANKINDIA_eq",
							"BATAINDIA_eq",
							"BEL_eq",
							"BEML_eq",
							"BFUTILITIE_eq",
							"BGRENERGY_eq",
							"BHARATFORG_eq",
							"BHUSANSTL_eq",
							"BIOCON_eq",
							"BOMDYEING_eq",
							"BOSCHLTD_eq",
							"BRFL_eq",
							"CANBK_eq",
							"CENTRALBK_eq",
							"CENTURYTEX_eq",
							"CESC_eq",
							"CHAMBLFERT_eq",
							"COLPAL_eq",
							"COREEDUTEC_eq",
							"CROMPGREAV_eq",
							"CUMMINSIND_eq",
							"DABUR_eq",
							"DCB_eq",
							"DELTACORP_eq",
							"DENABANK_eq",
							"DHANBANK_eq",
							"DISHTV_eq",
							"DIVISLAB_eq",
							"EDUCOMP_eq",
							"ESCORTS_eq",
							"ESSAROIL_eq",
							"EXIDEIND_eq",
							"FEDERALBNK_eq",
							"FINANTECH_eq",
							"FORTIS_eq",
							"FSL_eq",
							"GESHIP_eq",
							"GLAXO_eq",
							"GMDCLTD_eq",
							"GMRINFRA_eq",
							"GODREJIND_eq",
							"GSPL_eq",
							"GUJFLUORO_eq",
							"GVKPIL_eq",
							"HAVELLS_eq",
							"HCC_eq",
							"HDIL_eq",
							"HEXAWARE_eq",
							"HINDOILEXP_eq",
							"HINDPETRO_eq",
							"HINDZINC_eq",
							"IBREALEST_eq",
							"IDBI_eq",
							"IDEA_eq",
							"IFCI_eq",
							"IGL_eq",
							"INDHOTEL_eq",
							"INDIACEM_eq",
							"INDIAINFO_eq",
							"INDIANB_eq",
							"INDUSINDBK_eq",
							"IOB_eq",
							"IOC_eq",
							"IRB_eq",
							"IVRCLINFRA_eq",
							"JETAIRWAYS_eq",
							"JINDALSAW_eq",
							"JISLJALEQS_eq",
							"JPPOWER_eq",
							"JSWENERGY_eq",
							"JSWISPAT_eq",
							"JSWSTEEL_eq",
							"JUBLFOOD_eq",
							"KTKBANK_eq",
							"LICHSGFIN_eq",
							"LITL_eq",
							"LUPIN_eq",
							"MAX_eq",
							"MCDOWELLN_eq",
							"MCLEODRUSS_eq",
							"MERCATOR_eq",
							"MPHASIS_eq",
							"MRF_eq",
							"MRPL_eq",
							"MTNL_eq",
							"NAGAROIL_eq",
							"NATIONALUM_eq",
							"NCC_eq",
							"NHPC_eq",
							"NMDC_eq",
							"OFSS_eq",
							"OIL_eq",
							"ONMOBILE_eq",
							"OPTOCIRCUI_eq",
							"ORCHIDCHEM_eq",
							"ORIENTBANK_eq",
							"PANTALOONR_eq",
							"PATELENG_eq",
							"PATNI_eq",
							"PETRONET_eq",
							"PFC_eq",
							"PIRHEALTH_eq",
							"POLARIS_eq",
							"PRAJIND_eq",
							"PTC_eq",
							"PUNJLLOYD_eq",
							"RAYMOND_eq",
							"RECLTD_eq",
							"RENUKA_eq",
							"ROLTA_eq",
							"RUCHISOYA_eq",
							"SCI_eq",
							"SINTEX_eq",
							"SKUMARSYNF_eq",
							"SOBHA_eq",
							"SOUTHBANK_eq",
							"SREINFRA_eq",
							"SRTRANSFIN_eq",
							"STRTECH_eq",
							"SUNTV_eq",
							"SUZLON_eq",
							"SYNDIBANK_eq",
							"TATACHEM_eq",
							"TATACOFFEE_eq",
							"TATACOMM_eq",
							"TATAGLOBAL_eq",
							"TATAMTRDVR_eq",
							"TECHM_eq",
							"TITAN_eq",
							"TTKPRESTIG_eq ",
							"TTML_eq",
							"TVSMOTOR_eq",
							"UCOBANK_eq",
							"ULTRACEMCO_eq",
							"UNIONBANK_eq",
							"UNIPHOS_eq",
							"UNITECH_eq",
							"VIDEOIND_eq",
							"VIJAYABANK_eq",
							"VIPIND_eq",
							"VOLTAS_eq",
							"WELCORP_eq",
							"YESBANK_eq",
							"ZEEL_eq"
							},
						dbs = {RBI_DB, MCX_DB, NCDEX_DB, NSE_DB,
								BSE_DB, NSEFNO_DB, NSEOPT_DB,
								MYSCRIPS_DB, WORLDINDICES_DB,FIIDII_DB};
   private 	String[]    nifty100 = {nifty50[26], nifty50[29], nifty50[46], nifty50[48]},
						nifty150 = {nifty50[23], nifty50[25], nifty50[35], nifty50[36], nifty50[49]},
						nifty300 = {nifty50[28], nifty50[44], nifty50[2], nifty50[10], nifty50[13], nifty50[17], nifty50[14], nifty50[38]},
						nifty450 = {nifty50[11], nifty50[18], nifty50[8], nifty50[22], nifty50[24], nifty50[32], nifty50[41], nifty50[50]},
						nifty600 = {nifty50[15], nifty50[40], nifty50[43], nifty50[5], nifty50[19], nifty50[21], nifty50[33], nifty50[47]},
						nifty900 = {nifty50[3], nifty50[12], nifty50[42], nifty50[0], nifty50[45]},
						nifty1200 = {nifty50[4], nifty50[37], nifty50[7], nifty50[30]},
						nifty1800 = {nifty50[6], nifty50[27], nifty50[31], nifty50[39], nifty50[16]},
						nifty3000 = {nifty50[20], nifty50[1], nifty50[9], nifty50[34]};
  private Vector AllTables = new Vector();
  private Vector niftyLows = new Vector(),
  				niftyHighs = new Vector(),
                niftyTradeDates = new Vector(),
				niftyCloses = new Vector(),
				niftyVolumes = new Vector(),
				niftyPrevCMPs = new Vector(),
				niftyCMPs = new Vector(),
				niftyScrips = new Vector(),
				niftyPchange = new Vector(),
				curNiftyIndex = new Vector(),
				curNiftyVol = new Vector(),
				curNiftyPC = new Vector();
  private static double refValue;
  private static double[] sumOfPchanges = new double[53],
  						  sumOfPchanges2 = new double[167];
  private static int totalRecords,
  					noOfRowsMyScripsTable,
  					noOfRowsNiftyPCTable,
  					noOfRowsNonNiftyPCTable,
					NseFnoScripCount, niftyCount,
					totalTradeDates,
					bserows, nserows,
					bsecols, nsecols;

  private static String[][] BseSectors = new String[250][250],
							NseSectors = new String[250][250];

  private TableSorter sorter;
  private TableSorter2 sorter2;
  private JDBCAdapter dataBase;
  private JDBCAdapter2 dataBase2;
  private JComponent  compQueryOhlc, compQueryPerc;
  private JPanel chartContainerPanel, chartDisplayPanel, insertDataPanel, myEquityPanel, mainPanel, mainPanel2,
  			tabPanel3, centerBorderPanel, resultPanel, connectionPanel, connectionPanel2;
  private JScrollPane spOhlc, spPerc, scrollpaneMyScrips;
  private JTable tableMyScrips;
  private JComboBox fnoComboBox, fnoComboBox2, cmbNifty, cboMyEquity, cmbDatabase;
  private JTextArea   queryTextArea, queryTextArea2, texaNews, texaProfitLoss;
  private JButton fetchButton, fetchButton2, showConnectionInfoButton, showConnectionInfoButton2,
	  		btnMyEquityInsert, btnNas100, btnSp500, btnDjia,
			btnSsicomposite, btnNikkei, btnEurostox50, btnFtse100;
  private JLabel lblRbi, lblNifty, lblBseEquity, lblNseEquity, lblNseFno, lblMcxFut, lblNcdexFut, lblNseOpt,
			lblBseFno, lblBseOpt, lblNseCurFut, lblNseCurOpt, lblFiiDii, userNameLabel, userNameLabel2,
			passwordLabel, passwordLabel2, serverLabel, serverLabel2, driverLabel, driverLabel2,
			lblScrip, lblCompany,  lblLot,  lblSector, lblFaceValue,  lblBonusPerShare, lblAGMdate, lblURI,
			labelScripName, labelTransactionDate, labelTransactionType, labelPrice, labelQuantity,
			labelAmountPaid, labelTaxBrkPaid,
			lblNas100, lblSp500, lblDjia, lblDateWorldIndices,
			lblSsicomposite, lblNikkei, lblEurostox50, lblFtse100,
			lblPchangeDate, lblPchangeDateFrom,
			lblPchangeDate2, lblPchangeDateFrom2;
  private JTextField txtRbiFileName, txtNiftyFileName, txtBseEquityFileName, txtNseEquityFileName, txtNseFnoFileName,
			txtMcxFutFileName, txtNcdexFutFileName, txtNseOptFileName, txtBseFnoFileName, txtBseOptFileName,
			txtNseCurFutFileName, txtNseCurOptFileName, txtFiiDiiFileName, userNameField, userNameField2,
			passwordField, passwordField2, serverField, serverField2, driverField, driverField2,
			textFromDate1, textToDate1, textFromDate2, textToDate2, textScripName, textTransactionDate,
			textTransactionType, textPrice, textQuantity, textAmountPaid, textTaxBrkPaid,
			textNas100, textSp500, textDjia, textDateWorldIndices,
			textSsicomposite, textNikkei, textEurostox50, textFtse100,
			textPchangeDate , textPchangeDateFrom,
			textPchangeDate2 , textPchangeDateFrom2;

  public MoneyTree(String appTitle)
  {
    super(appTitle);
    setJMenuBar(createMenuBar());
    setContentPane(createContent());
  }
  public static void main(String[] args)
  {
   /*
    try
    {
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      UIManager.setLookAndFeel("javax.swing.plaf.basic.BasicLookAndFeel");
    }
    catch (Exception e1)
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
      }
    }
    */
    MetalTheme selectedTheme = new MyMetalTheme();
    MetalLookAndFeel.setCurrentTheme(selectedTheme);
    JFrame.setDefaultLookAndFeelDecorated(true);
    MoneyTree objectMoneyTree = new MoneyTree("Money Tree October 19 Disha Birthday");
    objectMoneyTree.pack();
    RefineryUtilities.centerFrameOnScreen(objectMoneyTree);
    objectMoneyTree.setVisible(true);
  }
  public JMenuBar createMenuBar()
  {
    JMenuBar localJMenuBar = new JMenuBar();
    JMenu localJMenu1 = new JMenu("File", true);
    localJMenu1.setMnemonic('F');
    JMenuItem localJMenuItem1 = new JMenuItem("Export to PDF...", 112);
    localJMenuItem1.setActionCommand("EXPORT_TO_PDF");
    localJMenuItem1.addActionListener(this);
    localJMenu1.add(localJMenuItem1);
    localJMenu1.addSeparator();
    JMenuItem localJMenuItem2 = new JMenuItem("Exit", 120);
    localJMenuItem2.setActionCommand("EXIT");
    localJMenuItem2.addActionListener(this);
    localJMenu1.add(localJMenuItem2);
    localJMenuBar.add(localJMenu1);
    JMenu localJMenu2 = new JMenu("Edit", false);
    localJMenuBar.add(localJMenu2);
    JMenuItem localJMenuItem3 = new JMenuItem("Copy", 67);
    localJMenuItem3.setActionCommand("COPY");
    localJMenuItem3.addActionListener(this);
    localJMenu2.add(localJMenuItem3);
    JMenu localJMenu3 = new JMenu("Theme", true);
    localJMenu3.setMnemonic('T');
    JCheckBoxMenuItem localJCheckBoxMenuItem1 = new JCheckBoxMenuItem("JFree", true);
    localJCheckBoxMenuItem1.setActionCommand("JFREE_THEME");
    localJCheckBoxMenuItem1.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem1);
    JCheckBoxMenuItem localJCheckBoxMenuItem2 = new JCheckBoxMenuItem("Darkness", false);
    localJCheckBoxMenuItem2.setActionCommand("DARKNESS_THEME");
    localJCheckBoxMenuItem2.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem2);
    JCheckBoxMenuItem localJCheckBoxMenuItem3 = new JCheckBoxMenuItem("Legacy", false);
    localJCheckBoxMenuItem3.setActionCommand("LEGACY_THEME");
    localJCheckBoxMenuItem3.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem3);
    ButtonGroup localButtonGroup = new ButtonGroup();
    localButtonGroup.add(localJCheckBoxMenuItem1);
    localButtonGroup.add(localJCheckBoxMenuItem2);
    localButtonGroup.add(localJCheckBoxMenuItem3);
    localJMenuBar.add(localJMenu3);
    return localJMenuBar;
  }
  public JPanel createContent()
  {
    JTree treeAllExch = new JTree(createTreeModel());
    treeAllExch.addTreeSelectionListener(this);

	JScrollPane scrollPaneTree = new JScrollPane(treeAllExch);
	JSplitPane splitPaneTreeAndChart = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPaneTreeAndChart.setLeftComponent(scrollPaneTree);
    splitPaneTreeAndChart.setRightComponent(createChartDisplayPanel());

	JPanel pnlTreeChartContainer = new JPanel(new BorderLayout());
    pnlTreeChartContainer.add(splitPaneTreeAndChart);

    JPanel pnlAllExchTab = new JPanel(new BorderLayout());
    pnlAllExchTab.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    pnlAllExchTab.add(pnlTreeChartContainer);

    JTabbedPane tabbedPaneApp = new JTabbedPane();
    tabbedPaneApp.add("All Exchange", pnlAllExchTab);
    tabbedPaneApp.add("Insert Data", ShowTabInsertDataPanel());
    tabbedPaneApp.add("My Scrips", ShowTabMyScripsPanel());
    tabbedPaneApp.add("Fundamentals", ShowTabFundamentalsPanel());
    tabbedPaneApp.add("Query Volume%", ShowTabQueryVolumePanel());
    tabbedPaneApp.add("Query CMP%", ShowTabQueryPercentagePanel());
    tabbedPaneApp.add("Nifty<100", ShowTabNiftyPanel100());
    tabbedPaneApp.add("Nifty<150", ShowTabNiftyPanel150());
	tabbedPaneApp.add("Nifty<300", ShowTabNiftyPanel300());
	tabbedPaneApp.add("Nifty<450", ShowTabNiftyPanel450());
    tabbedPaneApp.add("Nifty<600", ShowTabNiftyPanel600());
    tabbedPaneApp.add("Nifty<900", ShowTabNiftyPanel900());
	tabbedPaneApp.add("Nifty<1200", ShowTabNiftyPanel1200());
	tabbedPaneApp.add("Nifty<1800", ShowTabNiftyPanel1800());
    tabbedPaneApp.add("Nifty<3000", ShowTabNiftyPanel3000());
    tabbedPaneApp.add("Nifty%Change", ShowTabNiftyPChangePanel());
    tabbedPaneApp.add("NonNiftyFno%Change", ShowTabNonNiftyPChangePanel());

	JPanel pnltabbedPaneApp = new JPanel(new BorderLayout());
    pnltabbedPaneApp.setPreferredSize(new Dimension(1000,675));
    pnltabbedPaneApp.add(tabbedPaneApp);

    return pnltabbedPaneApp;
  }
  public TreeModel createTreeModel()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Exchange Names");
	/*
	localDefaultMutableTreeNode.add(createNodeName("Rbi", "Rbi"));
    localDefaultMutableTreeNode.add(createNodeName("Mcx", "Mcx"));
    localDefaultMutableTreeNode.add(createNodeName("Ncdex", "Ncdex"));
    localDefaultMutableTreeNode.add(createNodeName("Nse", "Nse"));
    localDefaultMutableTreeNode.add(createNodeName("Bse", "Bse"));
    localDefaultMutableTreeNode.add(createNodeName("NseFno", "NseFno"));
    localDefaultMutableTreeNode.add(createNodeName("NseOpt", "NseOpt"));
	localDefaultMutableTreeNode.add(createNodeName("WorldIndices", "WorldIndices"));
    localDefaultMutableTreeNode.add(createNodeName("FIIDII", "FIIDII"));
    */
	localDefaultMutableTreeNode.add(createNodeName2("RBI"));
    localDefaultMutableTreeNode.add(createNodeName2("MCX"));
    localDefaultMutableTreeNode.add(createNodeName2("NCDEX"));
    localDefaultMutableTreeNode.add(createNodeName2("NSE"));
    localDefaultMutableTreeNode.add(createNodeName2("BSE"));
    localDefaultMutableTreeNode.add(createNodeName2("NSEFNO"));
    localDefaultMutableTreeNode.add(createNodeName2("NSEOPT"));
	localDefaultMutableTreeNode.add(createNodeName2("WORLDINDICES"));
//    localDefaultMutableTreeNode.add(createNodeName2("FIIDII"));
    localDefaultMutableTreeNode.add(createNseFnOEquityNode());
    localDefaultMutableTreeNode.add(createMyNseEquityNode());
    localDefaultMutableTreeNode.add(createMyBseEquityNode());
    localDefaultMutableTreeNode.add(createMyNseFnoNode());
    localDefaultMutableTreeNode.add(createMyMcxNode());
    localDefaultMutableTreeNode.add(createMyNcdexNode());
    localDefaultMutableTreeNode.add(createBseSectorsNode());
    localDefaultMutableTreeNode.add(createNseSectorsNode());
    return new DefaultTreeModel(localDefaultMutableTreeNode);
  }
  public MutableTreeNode createNodeName2(String databaseName)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(databaseName);
    getFromCsvFile(databaseName);
    if(databaseName.equalsIgnoreCase("Rbi"))
       for(int i=0;RbiTables[i]!=null;++i){
               String s = new String( (String)RbiTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("Mcx"))
       for(int i=0;McxTables[i]!=null;++i){
           String s = new String( (String)McxTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("Ncdex"))
       for(int i=0;NcdexTables[i]!=null;++i){
           String s = new String( (String)NcdexTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("Nse"))
       for(int i=0;NseEqTables[i]!=null;++i){
           String s = new String( (String)NseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("Bse"))
       for(int i=1;BseEqTables[i]!=null;++i){
           String s = new String( (String)BseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("NseFno"))
       for(int i=0;NseFnoTables[i]!=null;++i){
           String s = new String( (String)NseFnoTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("NseOpt"))
       for(int i=0;NseOptTables[i]!=null;++i){
           String s = new String( (String)NseOptTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("FIIDII"))
       for(int i=0;FIIDIITables[i]!=null;++i){
           String s = new String( (String)FIIDIITables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("WorldIndices"))
       for(int i=0;WorldIndicesTables[i]!=null;++i){
           String s = new String( (String)WorldIndicesTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    return localDefaultMutableTreeNode;
  }
  public void getFromCsvFile(String databaseName)
  {
     	try	{
            String strLine = null, fileName = "_" + databaseName + "_ALL_TABLES.txt";
            String[] ar = null;
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            while( (strLine = br.readLine()) != null) {
                ar = strLine.split(",");
			    if(databaseName.equalsIgnoreCase("Rbi")) for(int i = 0; i< ar.length; ++i) RbiTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("Mcx")) for(int i = 0; i< ar.length; ++i) McxTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("Ncdex")) for(int i = 0; i< ar.length; ++i) NcdexTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("Nse")) for(int i = 0; i< ar.length; ++i) NseEqTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("Bse")) for(int i = 0; i< ar.length; ++i) BseEqTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("NseFno")) for(int i = 0; i< ar.length; ++i) NseFnoTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("NseOpt")) for(int i = 0; i< ar.length; ++i) NseOptTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("FIIDII")) for(int i = 0; i< ar.length; ++i) FIIDIITables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("WorldIndices")) for(int i = 0; i< ar.length; ++i) WorldIndicesTables[i] = ar[i];
            }
         br.close();
        } catch(Exception e) {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public MutableTreeNode createNodeName(String databaseName, String tableArrayPrefix)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(databaseName);
    //getAllDatabaseTables(databaseName, tableArrayPrefix);
    if(tableArrayPrefix.equals("Rbi"))
       for(int i=0;RbiTables[i]!=null;++i){
               String s = new String( (String)RbiTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("Mcx"))
       for(int i=0;McxTables[i]!=null;++i){
           String s = new String( (String)McxTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("Ncdex"))
       for(int i=0;NcdexTables[i]!=null;++i){
           String s = new String( (String)NcdexTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("Nse"))
       for(int i=0;NseEqTables[i]!=null;++i){
           String s = new String( (String)NseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("Bse"))
       for(int i=1;BseEqTables[i]!=null;++i){
           String s = new String( (String)BseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("NseFno"))
       for(int i=0;NseFnoTables[i]!=null;++i){
           String s = new String( (String)NseFnoTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("NseOpt"))
       for(int i=0;NseOptTables[i]!=null;++i){
           String s = new String( (String)NseOptTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("FIIDII"))
       for(int i=0;FIIDIITables[i]!=null;++i){
           String s = new String( (String)FIIDIITables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("WorldIndices"))
       for(int i=0;WorldIndicesTables[i]!=null;++i){
           String s = new String( (String)WorldIndicesTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    return localDefaultMutableTreeNode;
  }
  public void getAllDatabaseTables(String database, String tableArrayPrefix)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SHOW TABLES";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
                  String table = rs.getString("Tables_In_" + database);
                  if(tableArrayPrefix.equals("Bse")){
                       BseEqTables[totalRecords] = new String(table.substring(4));
                  }
                  else if(tableArrayPrefix.equals("Rbi")){
                       RbiTables[totalRecords] = new String(table.substring(4));
                  }
                  else if(tableArrayPrefix.equals("Nse")){
                       NseEqTables[totalRecords] = new String(table.substring(4));
                  }
                  else if(tableArrayPrefix.equals("NseFno")){
                       NseFnoTables[totalRecords] = new String(table.substring(7));
                  }
                  else if(tableArrayPrefix.equals("NseOpt")){
                       NseOptTables[totalRecords] = new String(table.substring(7));
                  }
                  else if(tableArrayPrefix.equals("Mcx")){
                       McxTables[totalRecords] = new String(table.substring(4));
                  }
                  else if(tableArrayPrefix.equals("Ncdex")){
                       NcdexTables[totalRecords] = new String(table.substring(6));
                  }
                  else if(tableArrayPrefix.equals("FIIDII")){
                       FIIDIITables[totalRecords] = new String(table.substring(6));
                  }
                  else if(tableArrayPrefix.equals("WorldIndices")){
                       WorldIndicesTables[totalRecords] = new String(table);
                  }
                  ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public MutableTreeNode createLeaf(String strClassName, String strDescription)
  {
    return new DefaultMutableTreeNode(new JavaClassAndFileName(strClassName, strDescription));
  }
  public MutableTreeNode createNseFnOEquityNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NSE_FNO_Equity");
    getFromFile("NSE_FNO_Equity.txt");
    actualNseFno = new String[NseFnoScripCount];
    for(int i=0;i<NseFnoScripCount;++i) {
        actualNseFno[i] = NseFnoFromFile[i];
        localDefaultMutableTreeNode.add(createLeaf((String)actualNseFno[i], (String)actualNseFno[i]));
    }
    return localDefaultMutableTreeNode;
  }
  public void getFromFile(String file){
        try{
            BufferedReader br = new BufferedReader( new FileReader(file));
            String oneLineFromFile = "";
            int count = 0, countbsecols = 1, countnsecols = 1;
            if(file.equals("NSE_FNO_Equity.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        NseFnoFromFile[count++] = oneLineFromFile;
                    ++NseFnoScripCount;
                 }
            }else if(file.equals("MY_NseEquity.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        mynseequity[count++] = oneLineFromFile;
                 }
            }else if(file.equals("MY_BseEquity.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        mybseequity[count++] = oneLineFromFile;
                 }
            }else if(file.equals("MY_NseFno.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        mynsefno[count++] = oneLineFromFile;
                 }
            }else if(file.equals("MY_Mcx.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        mymcx[count++] = oneLineFromFile;
                 }
            }else if(file.equals("MY_Ncdex.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        myncdex[count++] = oneLineFromFile;
                 }
            }else if(file.equals("BSE_SECTORS.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        BseSectors[count++][0] = oneLineFromFile;
                 }
            }else if(file.equals("NSE_SECTORS.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        NseSectors[count++][0] = oneLineFromFile;
                 }
            }else{
                 while( (oneLineFromFile = br.readLine()) != null){
                        BseSectors[bserows][countbsecols++] = oneLineFromFile;
                        NseSectors[nserows][countnsecols++] = oneLineFromFile;
                        ++bsecols;
                        ++nsecols;
                 }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
  }
  public MutableTreeNode createMyNseEquityNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("MyNseEquity");
    getFromFile("MY_NseEquity.txt");
    for(int i=0;mynseequity[i]!=null;++i){
        String s = new String( (String)mynseequity[i]);
            localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createMyBseEquityNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("MyBseEquity");
    getFromFile("MY_BseEquity.txt");
    for(int i=0;mybseequity[i]!=null;++i){
        String s = new String( (String)mybseequity[i]);
            localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createMyNseFnoNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("MyNseFno");
    getFromFile("MY_NseFno.txt");
    for(int i=0;mynsefno[i]!=null;++i){
        String s = new String( (String)mynsefno[i]);
            localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createMyMcxNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("MyMcx");
    getFromFile("MY_Mcx.txt");
    for(int i=0;mymcx[i]!=null;++i){
        String s = new String( (String)mymcx[i]);
            localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createMyNcdexNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("MyNcdex");
    getFromFile("MY_Ncdex.txt");
    for(int i=0;myncdex[i]!=null;++i){
        String s = new String( (String)myncdex[i]);
            localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createBseSectorsNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("BseSectors");
    getFromFile("BSE_SECTORS.txt");
    bserows = 0; bsecols = 0;
    for(int r = 0; BseSectors[r][0]!=null; ++ r){
        String sectorName = new String( (String)BseSectors[r][0]);
            localDefaultMutableTreeNode.add(createBseSectorsLeafNode(sectorName , r));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createNseSectorsNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NseSectors");
    getFromFile("NSE_SECTORS.txt");
    nserows = 0; nsecols = 0;
    for(int r = 0; NseSectors[r][0]!=null; ++ r){
        String sectorName = new String( (String)NseSectors[r][0]);
            localDefaultMutableTreeNode.add(createNseSectorsLeafNode(sectorName , r));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createBseSectorsLeafNode(String sectorName, int r)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(sectorName);
    getFromFile(sectorName + ".txt");
    for(int c = 1; c<=bsecols; ++ c){
        String sectorScrip = new String( (String)BseSectors[bserows][c]);
            localDefaultMutableTreeNode.add(createLeaf(sectorScrip, sectorScrip));
    }
    bsecols = 0;
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createNseSectorsLeafNode(String sectorName, int r)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(sectorName);
    getFromFile(sectorName + ".txt");
    for(int c = 1; c<=nsecols; ++ c){
        String sectorScrip = new String( (String)NseSectors[nserows][c]);
            localDefaultMutableTreeNode.add(createLeaf(sectorScrip, sectorScrip));
    }
    nsecols = 0;
    return localDefaultMutableTreeNode;
  }
  public JPanel createChartDisplayPanel()
  {
    chartDisplayPanel = new JPanel(new BorderLayout());
    chartDisplayPanel.setPreferredSize(new Dimension(500, 500));
    chartContainerPanel = new JPanel(new BorderLayout());
    //chartContainerPanel.setPreferredSize(new Dimension(500, 500));
    //chartContainerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createLineBorder(Color.black)));
    chartContainerPanel.add(createNoNodeSelectedPanel());
    //chartContainerPanel.add(createNiftyChart(nifty100));
    chartDisplayPanel.add(chartContainerPanel);
    return  chartDisplayPanel;
  }
  public JPanel createNoNodeSelectedPanel()
  {
     JPanel objEmptyChartPanel = new JPanel(new FlowLayout()) {
	       public String getToolTipText() {
				//return "(" + getWidth() + ", " + getHeight() + ")";
				return "Select a leaf in left tree";
			}
	 };
    ToolTipManager.sharedInstance().registerComponent(objEmptyChartPanel);
    objEmptyChartPanel.add(new JLabel("No Node selected"));
    objEmptyChartPanel.setPreferredSize(new Dimension(500, 500));
    return objEmptyChartPanel;
  }
  public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
  {
    String str = null;
    TreePath localTreePath = paramTreeSelectionEvent.getPath();
    Object lastLeaf = localTreePath.getLastPathComponent();
    if (lastLeaf != null)
    {
      Object node = localTreePath.getPathComponent(1);
      DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)lastLeaf;
      Object fileDescObject = localDefaultMutableTreeNode.getUserObject();
      if ((fileDescObject instanceof JavaClassAndFileName))
      {
        JavaClassAndFileName localJavaClassAndFileName = (JavaClassAndFileName)fileDescObject;
        String exchangeNode =null, scrip = null, database = null, tablePrefix = null;
        exchangeNode = (String)node.toString();
        scrip = (String)lastLeaf.toString();
        if( exchangeNode.equalsIgnoreCase("Bse")
                                                ||exchangeNode.equalsIgnoreCase("BSE_FNO_Equity")
                                                ||exchangeNode.equalsIgnoreCase("MyBseEquity")
                                                ||exchangeNode.equalsIgnoreCase("BseSectors")){
           tablePrefix = "bse_";
           database = BSE_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("NseFno")||exchangeNode.equalsIgnoreCase("MyNseFno")){
           tablePrefix = "nsefno_";
           database = NSEFNO_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("NseOpt")){
           tablePrefix = "nseopt_";
           database = NSEOPT_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("Mcx")||exchangeNode.equalsIgnoreCase("MyMcx")){
           tablePrefix = "mcx_";
           database = MCX_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("Ncdex")||exchangeNode.equalsIgnoreCase("MyNcdex")){
           tablePrefix = "ncdex_";
           database = NCDEX_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("Nse")
                                                    ||exchangeNode.equalsIgnoreCase("MyNseEquity")
                                                    ||exchangeNode.equalsIgnoreCase("NSE_FNO_Equity")
                                                    ||exchangeNode.equalsIgnoreCase("NseSectors")){
           tablePrefix = "nse_";
           database = NSE_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("Rbi")){
           tablePrefix = "rbi_";
           database = RBI_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("FIIDII")){
           tablePrefix = "";
           database = FIIDII_DB;
        }
        else if(exchangeNode.equalsIgnoreCase("WorldIndices")){
           tablePrefix = "";
           database = WORLDINDICES_DB;
        }
        SwingUtilities.invokeLater(new RunMoneyTree(this, localJavaClassAndFileName, database, tablePrefix));
      }
      else
      {
        this.chartContainerPanel.removeAll();
        this.chartContainerPanel.add(createNoNodeSelectedPanel());
        this.chartDisplayPanel.validate();
      }
    }
  }
  class RunMoneyTree implements Runnable
  {
    private MoneyTree objMoneyTree;
    private JavaClassAndFileName objJavaClassAndFileName;
    private String database;
    private String tablePrefix;

    public RunMoneyTree(MoneyTree objMoneyTree, JavaClassAndFileName objJavaClassAndFileName, String database, String tablePrefix)        //4.1
    {
      this.objMoneyTree = objMoneyTree;
      this.objJavaClassAndFileName = objJavaClassAndFileName;
      this.database = database;
      this.tablePrefix = tablePrefix;
    }

    public void run()
    {
      try
      {
        getRecords(database, tablePrefix + objJavaClassAndFileName.getClassName() , "tradedate, ohlc", "");
        Class localClass = Class.forName("MoneyTree");
        if(database.equalsIgnoreCase("Rbi"))
        {
                Method localMethod = localClass.getDeclaredMethod("createRbiChartPanel", (Class[])null);
                JPanel localJPanel = (JPanel)localMethod.invoke(null, (Object[])null);
                this.objMoneyTree.chartContainerPanel.removeAll();
                this.objMoneyTree.chartContainerPanel.add(localJPanel);
                this.objMoneyTree.chartDisplayPanel.validate();
        }
        else
        {
              Method localMethod = localClass.getDeclaredMethod("createMyChartPanel", (Class[])null);
              JPanel localJPanel = (JPanel)localMethod.invoke(null, (Object[])null);
              this.objMoneyTree.chartContainerPanel.removeAll();
              this.objMoneyTree.chartContainerPanel.add(localJPanel);
              this.objMoneyTree.chartDisplayPanel.validate();
        }
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localNoSuchMethodException.printStackTrace();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        localInvocationTargetException.printStackTrace();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
      }

    }
  }
  public static JPanel createMyChartPanel()
  {
    JFreeChart localJFreeChart = createChart(createDataset());
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);
    return localChartPanel;
  }
  public static JFreeChart createChart(OHLCDataset paramOHLCDataset)
  {
    StandardChartTheme theme = new  StandardChartTheme("deepak2");
        theme.setTitlePaint(Color.BLUE);
        theme.setSubtitlePaint(Color.BLUE);
        //theme.setLegendBackgroundPaint(Color.BLACK);
        //theme.setLegendItemPaint(Color.DARK_GRAY);
        theme.setChartBackgroundPaint(new Color(255, 255, 0, 0));
        theme.setPlotBackgroundPaint(new Color(255, 0, 255, 0));
        //theme.setPlotOutlinePaint(Color.ORANGE);
        //theme.setBaselinePaint(Color.PINK);
        //theme.setCrosshairPaint(Color.RED);
        //theme.setLabelLinkPaint(Color.LIGHT_GRAY);
        theme.setTickLabelPaint(Color.BLACK);
        theme.setAxisLabelPaint(Color.BLACK);
        //theme.setShadowPaint(Color.LIGHT_GRAY);
        theme.setItemLabelPaint(Color.BLACK);
        /*theme.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[] {
                                Color.decode("0xFFFF00"),
                                Color.decode("0x0036CC"), Color.decode("0xFF0000"),
                                Color.decode("0xFFFF7F"), Color.decode("0x6681CC"),
                                Color.decode("0xFF7F7F"), Color.decode("0xFFFFBF"),
                                Color.decode("0x99A6CC"), Color.decode("0xFFBFBF"),
                                Color.decode("0xA9A938"), Color.decode("0x2D4587")
                            },
                new Paint[] {
                                Color.decode("0xFFFF00"),
                                Color.decode("0x0036CC")
                            },
                new Stroke[] {new BasicStroke(2.0f)
                             },
                new Stroke[] {new BasicStroke(0.5f)
                             },
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE)); */
        //theme.setWallPaint(Color.LIGHT_GRAY);
        //theme.setErrorIndicatorPaint(Color.LIGHT_GRAY);
        //theme.setGridBandPaint(Color.LIGHT_GRAY);
        //theme.setGridBandAlternatePaint(Color.LIGHT_GRAY);
    ChartFactory.setChartTheme(theme);
    JFreeChart localJFreeChart = ChartFactory.createCandlestickChart( "", "Time", "Value", paramOHLCDataset, true);
    XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
    localXYPlot.setRangePannable(true);
    DateAxis localDateAxis = (DateAxis)localXYPlot.getDomainAxis();
    localDateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
    NumberAxis localNumberAxis = (NumberAxis)localXYPlot.getRangeAxis();
    localNumberAxis.setNumberFormatOverride(new DecimalFormat("Rs 0.00"));
    return localJFreeChart;
  }
  public JPanel ShowTabInsertDataPanel()
  {
	insertDataPanel = new JPanel(new GridLayout(1, 2, 20, 20));
	insertDataPanel.setPreferredSize(new Dimension(800,400));

    insertDataPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	JPanel filesPanel = new JPanel(new GridLayout(13, 2, 20, 20)),
			indicesPanel = new JPanel(new GridLayout(11, 3, 20, 20));
	filesPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	indicesPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	filesPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
	indicesPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));

	lblRbi = new JLabel("   FOREX");
	txtRbiFileName = new JTextField("DayWiseTurnover.csv", 20);
	lblNifty = new JLabel("   NIFTY");
	txtNiftyFileName = new JTextField("S&P CNX NIFTY" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + C_YEAR + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + C_YEAR + ".csv",20);
	lblBseEquity = new JLabel("   BSE EQ ");
	txtBseEquityFileName = new JTextField("eq" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + monthn[calendar.get(Calendar.MONTH)] + C_YEAR.substring(2) + ".csv", 20);
	lblNseEquity = new JLabel("   NSE EQ ");
	txtNseEquityFileName = new JTextField("cm" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "" +  months[calendar.get(Calendar.MONTH)] + C_YEAR + "bhav.csv", 20);
	lblNseFno = new JLabel("   NSE F & O ");
	txtNseFnoFileName = new JTextField("fo" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "" +  months[calendar.get(Calendar.MONTH)] + C_YEAR + "bhav.csv", 20);
	lblNseOpt = new JLabel("NSE O");
	txtNseOptFileName = new JTextField("", 20);
	lblMcxFut = new JLabel("   MCX ");
	txtMcxFutFileName = new JTextField(dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "" +  monthn[calendar.get(Calendar.MONTH)] + C_YEAR + ".csv", 20);
	lblNcdexFut = new JLabel("   NCDEX ");
	txtNcdexFutFileName = new JTextField(monthn[calendar.get(Calendar.MONTH)]+ "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "-" + C_YEAR + ".csv", 20);
	lblBseFno = new JLabel("BSE F & Sensex O");
	txtBseFnoFileName = new JTextField("", 20);
	lblBseOpt = new JLabel("BSE O");
	txtBseOptFileName = new JTextField("", 20);
	lblNseCurFut = new JLabel("NSE CF");
	txtNseCurFutFileName = new JTextField("", 20);
	lblNseCurOpt = new JLabel("NSE CO");
	txtNseCurOptFileName = new JTextField("", 20);
	lblFiiDii = new JLabel("FII DII");
	txtFiiDiiFileName = new JTextField("", 20);

	lblNas100 = new JLabel("   Nas100");
	textNas100 = new JTextField("", 20);
	lblSp500 = new JLabel("   Sp500");
	textSp500 = new JTextField("", 20);
	lblDjia = new JLabel("   Djia");
	textDjia = new JTextField("", 20);
	lblSsicomposite = new JLabel("   Ssicomposite");
	textSsicomposite = new JTextField("", 20);
	lblNikkei = new JLabel("   Nikkei");
	textNikkei = new JTextField("", 20);
	lblEurostox50 = new JLabel("   Eurostox50");
	textEurostox50 = new JTextField("", 20);
	lblFtse100 = new JLabel("   Ftse100");
	textFtse100 = new JTextField("", 20);

	lblDateWorldIndices = new JLabel("   TrasactionDate");
	textDateWorldIndices = new JTextField(C_YEAR + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)], 10);
	JLabel dummy = new JLabel("   O,H,L,C,V");
	btnNas100 = new JButton("USA Nasdaq100");
	btnSp500 = new JButton("USA S&P500");
	btnDjia = new JButton("USA DowjonesIA");
	btnSsicomposite = new JButton("China Ssicomposite");
	btnNikkei = new JButton("Japan Nikkei");
	btnEurostox50 = new JButton("Europe Eurostox50");
	btnFtse100 = new JButton("UK Ftse100");

	filesPanel.add(lblRbi);
	filesPanel.add(txtRbiFileName);
	filesPanel.add(lblMcxFut);
	filesPanel.add(txtMcxFutFileName);
	filesPanel.add(lblNcdexFut);
	filesPanel.add(txtNcdexFutFileName);
	filesPanel.add(lblNifty);
	filesPanel.add(txtNiftyFileName);
	filesPanel.add(lblNseEquity);
	filesPanel.add(txtNseEquityFileName);
	filesPanel.add(lblBseEquity);
	filesPanel.add(txtBseEquityFileName);
	filesPanel.add(lblNseFno);
	filesPanel.add(txtNseFnoFileName);
	//filesPanel.add(lblNseOpt);
	//filesPanel.add(txtNseOptFileName);
	//filesPanel.add(lblBseFno);
	//filesPanel.add(txtBseFnoFileName);
	//filesPanel.add(lblBseOpt);
	//filesPanel.add(txtBseOptFileName);
	//filesPanel.add(lblNseCurFut);
	//filesPanel.add(txtNseCurFutFileName);
	//filesPanel.add(lblNseCurOpt);
	//filesPanel.add(txtNseCurOptFileName);
	//filesPanel.add(lblFiiDii);
	//filesPanel.add(txtFiiDiiFileName);

	indicesPanel.add(lblDateWorldIndices);
	indicesPanel.add(textDateWorldIndices);
	indicesPanel.add(dummy);
	indicesPanel.add(lblNas100);
	indicesPanel.add(textNas100);
	indicesPanel.add(btnNas100);
	indicesPanel.add(lblSp500);
	indicesPanel.add(textSp500);
	indicesPanel.add(btnSp500);
	indicesPanel.add(lblDjia);
	indicesPanel.add(textDjia);
	indicesPanel.add(btnDjia);
	indicesPanel.add(lblSsicomposite);
	indicesPanel.add(textSsicomposite);
	indicesPanel.add(btnSsicomposite);
	indicesPanel.add(lblNikkei);
	indicesPanel.add(textNikkei);
	indicesPanel.add(btnNikkei);
	indicesPanel.add(lblEurostox50);
	indicesPanel.add(textEurostox50);
	indicesPanel.add(btnEurostox50);
	indicesPanel.add(lblFtse100);
	indicesPanel.add(textFtse100);
	indicesPanel.add(btnFtse100);

	insertDataPanel.add(filesPanel);
	insertDataPanel.add(indicesPanel);

	   btnNas100.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Nas100", textNas100.getText());
	            textNas100.setBackground(new Color(255, 0, 0));
	        }
	   });
	   btnSp500.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Sp500", textSp500.getText());
	            btnSp500.setBackground(new Color(255, 0, 0));
	        }
	   });
	   btnDjia.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Djia", textDjia.getText());
	            btnDjia.setBackground(new Color(255, 0, 0));
	        }
	   });
	   btnSsicomposite.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Ssicomposite", textSsicomposite.getText());
	        	btnSsicomposite.setBackground(new Color(255, 0, 0));
			}
	   });
	   btnNikkei.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Nikkei", textNikkei.getText());
	            btnNikkei.setBackground(new Color(255, 0, 0));
	        }
	   });
	   btnEurostox50.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Eurostox50", textEurostox50.getText());
	            btnEurostox50.setBackground(new Color(255, 0, 0));
	        }
	   });
	   btnFtse100.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            insertWorldIndicesToDatabase("Ftse100", textFtse100.getText());
	            btnFtse100.setBackground(new Color(255, 0, 0));
	        }
	   });
	   txtRbiFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertRbiCsvFileToDatabase(txtRbiFileName.getText());
	                txtRbiFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNiftyFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertNiftyCsvFileToDatabase(txtNiftyFileName.getText());
	                txtNiftyFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtBseEquityFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertBseEquityCsvFileToDatabase(txtBseEquityFileName.getText());
	                txtBseEquityFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNseEquityFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertNseEquityCsvFileToDatabase(txtNseEquityFileName.getText());
	                txtNseEquityFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNseFnoFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertNseFnoCsvFileToDatabase(txtNseFnoFileName.getText());
					System.out.println("**********************FUT Complete*******************************");
	                insertNseOptCsvFileToDatabase(txtNseFnoFileName.getText());
	                txtNseFnoFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNseOptFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                //insertNseOptCsvFileToDatabase(txtNseOptFileName.getText());
	                txtNseOptFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );

	   txtMcxFutFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertMcxFutCsvFileToDatabase(txtMcxFutFileName.getText());
	                txtMcxFutFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNcdexFutFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertNcdexFutCsvFileOldToDatabase(txtNcdexFutFileName.getText());
	                txtNcdexFutFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtFiiDiiFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                txtFiiDiiFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );

       return insertDataPanel;
  }
  public JPanel ShowTabMyScripsPanel()
  {
	myEquityPanel = new JPanel(new BorderLayout());
	myEquityPanel.setPreferredSize(new Dimension(800,400));
	myEquityPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	JPanel entryPanel = new  JPanel(new GridLayout(4, 4, 2, 2));
	JPanel tablePanel = new  JPanel(new BorderLayout());

	labelScripName = new JLabel("ScripName");
	textScripName = new JTextField("", 10 );
	labelTransactionDate  = new JLabel("TransactionDate");
	textTransactionDate = new JTextField(C_YEAR + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)], 10 );
	labelTransactionType = new JLabel("TransactionType   (b/s)");
	textTransactionType = new JTextField("b", 10 );
	labelPrice = new JLabel("Price");
	textPrice = new JTextField("", 10 );
	labelQuantity = new JLabel("Quantity");
	textQuantity = new JTextField("", 10 );
	labelAmountPaid = new JLabel("Amount (Price X Quantity) ");
	textAmountPaid = new JTextField("", 10 );
	labelTaxBrkPaid = new JLabel("TaxBrkPaid");
	textTaxBrkPaid = new JTextField("0.00", 10 );

	btnMyEquityInsert = new JButton("INSERT");
	btnMyEquityInsert.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			myEquityInsert();
		}
	});
   textPrice.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent ke) {
            int key =ke.getKeyCode();
            if(key==KeyEvent.VK_ENTER){
                float q = Float.valueOf(textQuantity.getText()).floatValue();
                float p = Float.valueOf(textPrice.getText()).floatValue();
                float paid = q*p;
                float brk = (paid/100)*0.75F;
 				textAmountPaid.setText(String.valueOf(paid));
 				textTaxBrkPaid.setText(String.valueOf(brk));
            }
        }

    }
   );
	String[] brokers = {"icd","ib","sk","rg"};
	cboMyEquity = new JComboBox(brokers);
	cboMyEquity.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JComboBox cb = (JComboBox)e.getSource();
            String item = (String)cb.getSelectedItem();
            System.out.println(item);
	    }
	});

	tableMyScrips = new JTable(new MyScripsTableModel());
	tableMyScrips.setAutoCreateRowSorter(true);
	scrollpaneMyScrips = new JScrollPane(tableMyScrips);

	entryPanel.add(labelTransactionDate);
	entryPanel.add(textTransactionDate);
	entryPanel.add(labelQuantity);
	entryPanel.add(textQuantity);
	entryPanel.add(labelScripName);
	entryPanel.add(textScripName);
	entryPanel.add(labelPrice);
	entryPanel.add(textPrice);
	entryPanel.add(labelTransactionType);
	entryPanel.add(textTransactionType);
	entryPanel.add(labelAmountPaid);
	entryPanel.add(textAmountPaid);
	entryPanel.add(cboMyEquity);
	entryPanel.add(btnMyEquityInsert);
	entryPanel.add(labelTaxBrkPaid);
	entryPanel.add(textTaxBrkPaid);

	myEquityPanel.add(entryPanel, BorderLayout.NORTH);
	myEquityPanel.add(scrollpaneMyScrips, BorderLayout.CENTER);
	return myEquityPanel;
  }
  public JPanel ShowTabFundamentalsPanel() {
       tabPanel3 = new JPanel(new BorderLayout());
       tabPanel3.setPreferredSize(new Dimension(800,600));
       tabPanel3.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	   JPanel pnlBtnCmb = new  JPanel(new BorderLayout());
       centerBorderPanel = new  JPanel(new BorderLayout());
       centerBorderPanel.setPreferredSize(new Dimension(600,600));
       JPanel stillPanel = new JPanel(new GridLayout(4, 2, 10, 30));
       resultPanel = new  JPanel(new BorderLayout());
       resultPanel.setPreferredSize(new Dimension(600,600));

	   cmbDatabase = new JComboBox(dbs);
       cmbNifty = new JComboBox(nifty50);
       lblScrip = new JLabel("Scrip ");
       lblCompany = new JLabel("Company ");
       lblLot = new JLabel("Lot ");
       lblSector = new JLabel("Sector ");
       lblFaceValue = new JLabel("Face Value ");
       lblBonusPerShare = new JLabel("Bonus Per Share ");
       lblAGMdate = new JLabel("AGM Date ");
       lblURI = new JLabel("URI ");
       texaNews = new JTextArea("News ", 6, 20);
       texaNews.setBackground(new Color(150, 150, 150));
       texaNews.setLineWrap(true);
       texaProfitLoss = new JTextArea("Profit Loss ", 6, 20);
       texaProfitLoss.setBackground(new Color(150, 150, 150));
       texaProfitLoss.setLineWrap(true);

       cmbNifty.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                     JComboBox cb = (JComboBox)e.getSource();
                     String item = (String)cb.getSelectedItem(), scpno = null ,scp =null;
                     getDetailsFromXlsFile("_company_info.xls", item);
                     //for(int i=0; temp[i]!=null;i++) System.out.println(temp[i]);
                     if(temp[0] != null) { scpno = temp[0].substring(0,2); scp = temp[0].substring(2); }
					 lblScrip.setText("Scrip=     " + scpno + "         " + scp);
                     lblCompany.setText("Company=     "+ temp[1]);
                     lblLot.setText("Lot=     " + temp[2]);
                     lblSector.setText("Sector=     " + temp[3]);
                     lblFaceValue.setText("FaceValue=     " + temp[4]);
                     String[] b = null;
					 //b = temp[5].split(">");
					 //float sum = 0;
                     //for(int i = 0; b[i] != null; i++) sum+= Float.valueOf(b[i]).floatValue();

                     lblBonusPerShare.setText("EarningPerShare=     " + temp[5] );
                     lblAGMdate.setText("AGMDate=     " + temp[6]);
                     lblURI.setText("URI=     " + temp[7]);
                     texaNews.setText("News=\n" + temp[8]);
                     String[] qtrs = null;
					 qtrs = temp[9].split(">");
                     texaProfitLoss.setText("NetProfitLoss=\n" + qtrs[4] + "\t" + qtrs[5] + "\t" + qtrs[6]  );
              }
       });
       cmbDatabase.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String item = (String)cb.getSelectedItem();
              	exportToFile(item);
              }
       });
       centerBorderPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
       resultPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	   stillPanel.add(lblScrip);
       stillPanel.add(lblCompany);
       stillPanel.add(lblLot);
       stillPanel.add(lblSector);
       stillPanel.add(lblFaceValue);
       stillPanel.add(lblBonusPerShare);
       stillPanel.add(lblAGMdate);
       stillPanel.add(lblURI);
       resultPanel.add(texaNews, BorderLayout.NORTH);
       resultPanel.add(stillPanel,  BorderLayout.CENTER);
       resultPanel.add(texaProfitLoss, BorderLayout.SOUTH);
       pnlBtnCmb.add(cmbDatabase, BorderLayout.WEST);
       pnlBtnCmb.add(cmbNifty, BorderLayout.EAST);
       centerBorderPanel.add(resultPanel, BorderLayout.CENTER);
       centerBorderPanel.add(pnlBtnCmb, BorderLayout.SOUTH);
       JPanel wPanel = new JPanel(), nPanel = new JPanel(), ePanel = new JPanel(), sPanel = new JPanel();
       wPanel.setPreferredSize(new Dimension(100,400));
       nPanel.setPreferredSize(new Dimension(800,10));
       ePanel.setPreferredSize(new Dimension(100,400));
       sPanel.setPreferredSize(new Dimension(800,180));
       tabPanel3.add(wPanel, BorderLayout.WEST);
       tabPanel3.add(nPanel, BorderLayout.NORTH);
       tabPanel3.add(ePanel, BorderLayout.EAST);
       tabPanel3.add(sPanel, BorderLayout.SOUTH);
       tabPanel3.add(centerBorderPanel, BorderLayout.CENTER);
       return tabPanel3;
  }
  public void getDetailsFromXlsFile(String fileName, String item)
  {
  	 try {
	    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileName));
	    String scrip = null, company = null, lot = null, sector = null, facevalue = null;
	    String eps = null, agmdate = null, uri = null, news = null, netpl =null;
		String[] temp_scrip = null;
			for (int k = 0; k < wb.getNumberOfSheets(); k++) {
				HSSFSheet sheet = wb.getSheetAt(k);
				int rows = sheet.getPhysicalNumberOfRows();
				//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows
				//		+ " row(s).");
				for (int r = 0; r < rows; r++) {
					HSSFRow row = sheet.getRow(r);
					if (row == null) {
						continue;
					}

					int cells = row.getPhysicalNumberOfCells();
					//System.out.println("\nROW " + row.getRowNum() + " has " + cells
					//		+ " cell(s).");

					for (int c = 0; c < cells; c++) {
						HSSFCell cell = row.getCell(c);
						String value = null;

						switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_NUMERIC:
		                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
		                            Calendar cal = Calendar.getInstance();
		                            cal.setTime(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
		                            value = (String.valueOf(cal.get(Calendar.YEAR)));
		                            value =  value + "_" + (cal.get(Calendar.MONTH)+1) + "_" + cal.get(Calendar.DAY_OF_MONTH);
		                        } else {
		                            value = "" + cell.getNumericCellValue();
		                        }
								break;
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getRichStringCellValue().getString();
								break;
							default:
						}
					//System.out.println("CELL col=" + cell.getColumnIndex() + " VALUE=" + value);

					if(cell.getColumnIndex() == 0) scrip = value.trim();
					else if(cell.getColumnIndex() == 1) company = value.trim();
					else if(cell.getColumnIndex() == 2) lot = value.trim();
					else if(cell.getColumnIndex() == 3) sector = value.trim();
					else if(cell.getColumnIndex() == 4) facevalue = value.trim();
					else if(cell.getColumnIndex() == 5) eps = value.trim();
					else if(cell.getColumnIndex() == 6) agmdate = value.trim();
					else if(cell.getColumnIndex() == 7) uri = value.trim();
					else if(cell.getColumnIndex() == 8) news = value.trim();
					else if(cell.getColumnIndex() == 9) netpl = value.trim();
		            }//each column
		            temp_scrip = item.split("_");
					if(temp_scrip[0].equalsIgnoreCase(scrip.substring(2))) {
						temp[0] = scrip;
						temp[1] = company;
						temp[2] = lot;
						temp[3] = sector;
						temp[4] = facevalue;
						temp[5] = eps;
						temp[6] = agmdate;
						temp[7] = uri;
						temp[8] = news;
						temp[9] = netpl;
					}
					//System.out.println(item.substring(0, item.indexOf('_'))+ scrip.substring(2));
					if(scrip.substring(2).equalsIgnoreCase(item.substring(0, item.indexOf('_')))) break;
					//System.out.println(scrip + company + lot + sector + facevalue + eps + agmdate + uri + news + netpl);
				}//each row
			}//each sheet
        }catch(Exception e) {
            //e.printStackTrace();
        }
  }
  public void exportToFile(String filename) {
		try	{
			BufferedWriter bw = new BufferedWriter(new FileWriter("_" + filename + "_ALL_TABLES" + ".txt"));
			getAllTablesFromDatabase(filename);
			for(int i = 0; i < AllTables.size(); i++) {
				System.out.println(i + " " + AllTables.get(i));
				String s = (String)AllTables.get(i);
				bw.write(s + ',');
			}
			bw.flush();
			bw.close();
			AllTables.removeAllElements();
		}
		catch(IOException ioe) {
			System.out.println("Exception Caught : " +ioe.getMessage());
		}

	}
  public void getAllTablesFromDatabase(String database)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SHOW TABLES";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String table = rs.getString("Tables_In_" + database);
			AllTables.add(table.substring(table.indexOf("_") + 1));
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public JPanel ShowTabQueryVolumePanel() {
	mainPanel = new JPanel(new BorderLayout());
	mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	createConnectionDialog();
	showConnectionInfoButton = new JButton("Config");
	showConnectionInfoButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		activateConnectionDialog();
		}
	});

	fetchButton = new JButton("Fetch");
	fetchButton.setBorder(new BevelBorder(BevelBorder.RAISED));
	fetchButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fetch();
		}
	});
	fnoComboBox = new JComboBox(actualNseFno);
	fnoComboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			String item = (String)cb.getSelectedItem();
			appendToTextAreaNseVolume(item);
		}
	});
    textFromDate1 = new JTextField(FROM_DATE, 20);
	textToDate1 = new JTextField(TO_DATE, 20);
	queryTextArea = new JTextArea("SELECT * FROM ", 4, 65);
	queryTextArea.setLineWrap(true);
	queryTextArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
	compQueryOhlc = new JScrollPane(queryTextArea);
	compQueryOhlc.setBorder(new BevelBorder(BevelBorder.RAISED));
	spOhlc = createTable();
	spOhlc.setBorder(new BevelBorder(BevelBorder.LOWERED));

	JPanel controls = new JPanel(new BorderLayout());
	JPanel dateCon = new JPanel();
	controls.add(showConnectionInfoButton, BorderLayout.WEST);
	controls.add(queryTextArea, BorderLayout.CENTER);
	dateCon.add(textFromDate1);
	dateCon.add(textToDate1);
	dateCon.add(fnoComboBox);
	controls.add(dateCon, BorderLayout.NORTH);
    controls.add(fetchButton, BorderLayout.EAST);

	JPanel result = new JPanel(new BorderLayout());
	result.add(spOhlc, BorderLayout.CENTER);
	mainPanel.add(controls, BorderLayout.NORTH);
	mainPanel.add(result, BorderLayout.CENTER);
    activateConnectionDialog();

	return mainPanel;
  }
  public JPanel ShowTabQueryPercentagePanel() {
	mainPanel2 = new JPanel(new BorderLayout());
	mainPanel2.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	createConnectionDialog2();

	showConnectionInfoButton2 = new JButton("Config");
	showConnectionInfoButton2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	           activateConnectionDialog2();
	    }
	});

	fetchButton2 = new JButton("Fetch");
	fetchButton2.setBorder(new BevelBorder(BevelBorder.RAISED));
	fetchButton2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fetch2();
		}
		});
	fnoComboBox2 = new JComboBox(actualNseFno);
	fnoComboBox2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			String item = (String)cb.getSelectedItem();
			appendToTextAreaNsePercentage(item);
		}
	});
	textFromDate2 = new JTextField(FROM_DATE, 20);
	textToDate2 = new JTextField(TO_DATE, 20);
	queryTextArea2 = new JTextArea("SELECT * FROM ",3 ,40);
	queryTextArea2.setLineWrap(true);
	queryTextArea2.setBorder(new BevelBorder(BevelBorder.LOWERED));

	compQueryPerc = new JScrollPane(queryTextArea2);
	compQueryPerc.setBorder(new BevelBorder(BevelBorder.RAISED));

	spPerc = createTable2();
	spPerc.setBorder(new BevelBorder(BevelBorder.LOWERED));

	JPanel controls2 = new JPanel(new BorderLayout());
	JPanel dateCont = new JPanel();
	controls2.add(showConnectionInfoButton2, BorderLayout.WEST);
	controls2.add(queryTextArea2,BorderLayout.CENTER);
	dateCont.add(textFromDate2);
	dateCont.add(textToDate2);
	dateCont.add(fnoComboBox2);
	controls2.add(dateCont, BorderLayout.NORTH);
	controls2.add(fetchButton2, BorderLayout.EAST);

	JPanel result2 = new JPanel(new BorderLayout());
	result2.add(spPerc, BorderLayout.CENTER);
	mainPanel2.add(controls2, BorderLayout.NORTH);
	mainPanel2.add(result2, BorderLayout.CENTER);
	activateConnectionDialog2();
	return mainPanel2;
  }
  public JPanel ShowTabNiftyPanel100()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty100), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel150()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty150), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel300()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty300), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel450()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty450), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel600()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty600), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel900()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty900), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel1200()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty1200), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPanel1800()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty1800), BorderLayout.CENTER);
  	return main;
  }
 public JPanel ShowTabNiftyPanel3000()
  {
  	JPanel main = new JPanel(new BorderLayout());
  	main.add(createNiftyChart(nifty3000), BorderLayout.CENTER);
  	return main;
  }
  public JPanel ShowTabNiftyPChangePanel()
  {
	JPanel niftyPCPanel = new JPanel(new BorderLayout()),
			pnlInsert = new JPanel(new GridLayout(1, 4));
	niftyPCPanel.setPreferredSize(new Dimension(800,400));
	niftyPCPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

	lblPchangeDate = new JLabel("   Enter Pchange date To DB");
	textPchangeDate = new JTextField("'" + C_YEAR + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "'", 10 );
	lblPchangeDateFrom = new JLabel("   Enter Pchange date To Retrieve");
	textPchangeDateFrom = new JTextField(PC_FROM);
	pnlInsert.add(lblPchangeDate);
	pnlInsert.add(textPchangeDate);
	pnlInsert.add(lblPchangeDateFrom);
	pnlInsert.add(textPchangeDateFrom);

	JTable tableNiftyPC = new JTable(new NiftyPCTableModel());
	TableCellRenderer NiftyPCrenderer = new NiftyPcTableCellRenderer();
    tableNiftyPC.setDefaultRenderer( Double.class, NiftyPCrenderer );

	tableNiftyPC.setAutoCreateRowSorter(true);
	tableNiftyPC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	JScrollPane scrollpaneTableNiftyPC = new JScrollPane(tableNiftyPC);
	niftyPCPanel.add(pnlInsert, BorderLayout.NORTH);
	niftyPCPanel.add(scrollpaneTableNiftyPC, BorderLayout.CENTER);

	textPchangeDate.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	            	getNiftyIndex(textPchangeDate.getText());
	            	calculateIndexPchange();
		    		for(int t = 0; t < nifty50.length; t++)
						getNiftyCMPs("nse_" + nifty50[t], textPchangeDate.getText());
					calculatePchange(nifty50.length);
		        	insertPchangeToDatabase(textPchangeDate.getText());
		        	textPchangeDate.setBackground(new Color(255, 0, 255));
		        	System.out.println(textPchangeDate.getText());
	            }
	        }

	    }
	);
	textPchangeDateFrom.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
                 	System.out.println(textPchangeDateFrom.getText());
	            }
	        }

	    }
	);
	return niftyPCPanel;
  }
  public JPanel ShowTabNonNiftyPChangePanel()
  {
	JPanel niftyPCPanel = new JPanel(new BorderLayout()),
			pnlInsert = new JPanel(new GridLayout(1, 4));
	niftyPCPanel.setPreferredSize(new Dimension(800,400));
	niftyPCPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

	lblPchangeDate2 = new JLabel("   Enter Pchange date To DB");
	textPchangeDate2 = new JTextField("'" + C_YEAR + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "'", 10 );
	lblPchangeDateFrom2 = new JLabel("   Enter Pchange date To Retrieve");
	textPchangeDateFrom2 = new JTextField(PC_FROM);
	pnlInsert.add(lblPchangeDate2);
	pnlInsert.add(textPchangeDate2);
	pnlInsert.add(lblPchangeDateFrom2);
	pnlInsert.add(textPchangeDateFrom2);

	JTable tableNiftyPC = new JTable(new nonNiftyPCTableModel());
	TableCellRenderer NiftyPCrenderer = new NiftyPcTableCellRenderer();
    tableNiftyPC.setDefaultRenderer( Double.class, NiftyPCrenderer );

	tableNiftyPC.setAutoCreateRowSorter(true);
	tableNiftyPC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	JScrollPane scrollpaneTableNiftyPC = new JScrollPane(tableNiftyPC);
	niftyPCPanel.add(pnlInsert, BorderLayout.NORTH);
	niftyPCPanel.add(scrollpaneTableNiftyPC, BorderLayout.CENTER);

	textPchangeDate2.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	            	getNiftyIndex(textPchangeDate2.getText());
	            	calculateIndexPchange();
		    		//for(int t = 0; t < nifty50.length; t++)
					//	getNiftyCMPs("nse_" + nifty50[t], textPchangeDate.getText());
		    		for(int t = 0; t < nonNifty.length; t++) {
						getNiftyCMPs("nse_" + nonNifty[t], textPchangeDate2.getText());
					//System.out.println(niftyScrips);
					//System.out.println(niftyCMPs);
					//System.out.println(niftyPrevCMPs);
					}
					calculatePchange(nonNifty.length);
		        	//insertPchangeToDatabase(textPchangeDate.getText());
		        	insertNonNiftyPchangeToDatabase(textPchangeDate2.getText());
		        	textPchangeDate2.setBackground(new Color(0, 255, 255));
		        	System.out.println(textPchangeDate2.getText());
	            }
	        }

	    }
	);
	textPchangeDateFrom2.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
                 	System.out.println(textPchangeDateFrom2.getText());
	            }
	        }

	    }
	);
	return niftyPCPanel;
  }
  public void getNiftyIndex(String trdate)
  {
	   try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT ohlc FROM nse_nifty WHERE tradedate <= " + trdate  + " ORDER BY tradedate DESC LIMIT 2";
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String ohlcdata = rs.getString("ohlc");
			String[] oh = null;
			oh = ohlcdata.split(",");
			curNiftyIndex.add(oh[3]);
			curNiftyVol.add(oh[4]);
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
  }
  public void getNiftyCMPs(String tableName, String trdate)
  {
       try {
        Class.forName("com.mysql.jdbc.Driver");
        boolean recordNo = true;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT ohlc FROM " + tableName + " WHERE tradedate <= " + trdate + " ORDER BY tradedate DESC LIMIT 2";
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String ohlcdata = rs.getString("ohlc");
			String[] oh = null;
			oh = ohlcdata.split(",");
			if(recordNo) {
			    String[] scr = tableName.split("_");
				niftyScrips.add(scr[1]);
				niftyCMPs.add(oh[3]);
				recordNo = false;
			} else {
				niftyPrevCMPs.add(oh[3]);
			}
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
  }
	public void calculatePchange(int scripCount)
	{
		for(int i = 0; i < scripCount; ++i) {
			double prev, cur, pc, diff;
			String ps = niftyPrevCMPs.get(i).toString(), s = niftyCMPs.get(i).toString();
			prev = Double.valueOf(ps).doubleValue();
			cur = Double.valueOf(s).doubleValue();
			diff = cur - prev;
			pc = (diff/prev)*100;
			niftyPchange.add(pc);
		}
	}
	public void calculateIndexPchange()
	{
		double np, n , nvp, nv, dn, dnv, pcn, pcv;
		String nps = curNiftyIndex.get(1).toString(),
			   ns = curNiftyIndex.get(0).toString(),
			   nvps = curNiftyVol.get(1).toString(),
			   nvs = curNiftyVol.get(0).toString();
		np = Double.valueOf(nps).doubleValue();
		n = Double.valueOf(ns).doubleValue();
		nvp = Double.valueOf(nvps).doubleValue();
		nv = Double.valueOf(nvs).doubleValue();
		dn = n - np;
		pcn = (dn/np)*100;
		dnv = nv - nvp;
		pcv = (dnv/nvp)*100;
		curNiftyPC.add(pcn);
		curNiftyPC.add(pcv);
		//System.out.println(curNiftyPC);
	}
	public void insertPchangeToDatabase(String trdate)
	{
		String allscr = "", beg = "INSERT INTO niftypc VALUES ( null, ";
		String nif = curNiftyPC.get(0).toString() + ", ";
		String nifv = curNiftyPC.get(1).toString();
			//HEROHONDA_eq Last trade date 5 aug 11
			//INFOSYSTCH_eq Last trade date 28 jun 11
			//COALINDIA_eq First trade date  4 nov 10
		for(int i =0; i< 51; i++) allscr += ", " + niftyPchange.get(i).toString();
		String sql = beg
				   + trdate + ", "
				   + nif
				   + nifv
				   + allscr
				   + " )";
		//System.out.println(sql);
     	try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
            Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
         	connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
        	niftyScrips.removeAllElements();
        	niftyPrevCMPs.removeAllElements();
        	niftyCMPs.removeAllElements();
        	niftyPchange.removeAllElements();
			curNiftyIndex.removeAllElements();
        	curNiftyVol.removeAllElements();
        	curNiftyPC.removeAllElements();
		}
	}
	public void insertNonNiftyPchangeToDatabase(String trdate)
	{
		String allscr = "", beg = "INSERT INTO nonniftypc VALUES ( null, ";
		String nif = curNiftyPC.get(0).toString() + ", ";
		String nifv = curNiftyPC.get(1).toString();
			//HEROHONDA_eq Last trade date 5 aug 11
			//INFOSYSTCH_eq Last trade date 28 jun 11
			//COALINDIA_eq First trade date  4 nov 10
		for(int i =0; i< 165; i++) allscr += ", " + niftyPchange.get(i).toString();
		String sql = beg
				   + trdate + ", "
				   + nif
				   + nifv
				   + allscr
				   + " )";
		//System.out.println(sql);
     	try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
            Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
         	connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
        	niftyScrips.removeAllElements();
        	niftyPrevCMPs.removeAllElements();
        	niftyCMPs.removeAllElements();
        	niftyPchange.removeAllElements();
			curNiftyIndex.removeAllElements();
        	curNiftyVol.removeAllElements();
        	curNiftyPC.removeAllElements();
		}
	}
	public void createNonNiftyFnoPchangeTable()
	{
		String allscr = "", beg = "CREATE TABLE IF NOT EXISTS nonniftypc ( id int(11) NOT NULL AUTO_INCREMENT, ";
			  beg += "tradedate date DEFAULT NULL, ";
			  beg += "NIFTY double DEFAULT NULL, ";
			  beg += "NIFTYVOL double DEFAULT NULL, ";
		for(int i = 0; i < nonNifty.length; ++i) {
			String[] scrip = nonNifty[i].split("_");
			allscr += scrip[0] + " double DEFAULT NULL, " ;
		}
		String sql = beg
				   + allscr
				   + " PRIMARY KEY (id))";
     	try
     	{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
            Statement statement = connection.createStatement();
            //System.out.println(sql);
			statement.executeUpdate(sql);
         	connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
  public JPanel createNiftyChart(String[] scripArray)
  {
 	JFreeChart localJFreeChart = createNifChart(createNiftyLowsDataset(scripArray));
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);
    return localChartPanel;
  }

  public void getRecords(String database, String tableName, String columns, String condition)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT " + columns + " FROM " + tableName + condition;
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
                          String date = rs.getString("tradedate");
                          String ohlcdata = rs.getString("ohlc");
                          dates[totalRecords] = date;
                          ohlc[totalRecords] = ohlcdata;
                          ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        ex.printStackTrace();
    }
  }

 public void getNifty(String tableName, int scripno)
 {
       try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT tradedate, ohlc FROM " + tableName + " WHERE tradedate BETWEEN " + FROM_DATE_FOR_CHARTS + " AND " + TO_DATE;
        //System.out.println(sql);
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String date = rs.getString("tradedate");
			String ohlcdata = rs.getString("ohlc");
			String[] oh = null;
			oh = ohlcdata.split(",");
			niftyTradeDates.add(date);
			niftyHighs.add(oh[1]);
			niftyLows.add(oh[2]);
			niftyCloses.add(oh[3]);
			niftyVolumes.add(oh[4]);
			++totalRecords;
        }
        ++niftyCount;
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
 }

  public XYDataset createNiftyLowsDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyLowsTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
  public XYDataset createNiftyClosesDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyClosesTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
  public XYDataset createNiftyVolumesDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyVolumesTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
 public TimeSeries createNiftyVolumesTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyVolumes.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }
  public TimeSeries createNiftyClosesTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyCloses.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }
  public TimeSeries createNiftyLowsTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyLows.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }

  public JFreeChart createNifChart(XYDataset paramXYDataset)
  {
  	//theme
    StandardChartTheme theme = new  StandardChartTheme("deepak3");
    theme.setTitlePaint(Color.BLUE);
    theme.setSubtitlePaint(Color.BLUE);
    theme.setChartBackgroundPaint(new Color(255, 255, 0, 0));
    theme.setPlotBackgroundPaint(new Color(255, 0, 255, 0));
    theme.setAxisLabelPaint(Color.BLACK);
    theme.setItemLabelPaint(Color.BLACK);
    ChartFactory.setChartTheme(theme);

    JFreeChart localJFreeChart = ChartFactory.createTimeSeriesChart("", "Date", "Price Per Unit", paramXYDataset, true, true, false);

	XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
    localXYPlot.setDomainPannable(true);
    localXYPlot.setRangePannable(true);
    localXYPlot.setDomainCrosshairVisible(true);
    localXYPlot.setRangeCrosshairVisible(true);
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    //chart shape points
    if ((localXYItemRenderer instanceof XYLineAndShapeRenderer))
    {
      Object localObject = (XYLineAndShapeRenderer)localXYItemRenderer;
      ((XYLineAndShapeRenderer)localObject).setSeriesShape(0, new Ellipse2D.Double(-3.0D, -3.0D, 6.0D, 6.0D));
      ((XYLineAndShapeRenderer)localObject).setDrawOutlines(true);
      ((XYLineAndShapeRenderer)localObject).setUseFillPaint(true);
      ((XYLineAndShapeRenderer)localObject).setBaseFillPaint(Color.yellow);
      ((XYLineAndShapeRenderer)localObject).setSeriesStroke(0, new BasicStroke(1.0F));
      ((XYLineAndShapeRenderer)localObject).setSeriesOutlineStroke(0, new BasicStroke(1.0F));
      ((XYLineAndShapeRenderer)localObject).setBaseShapesVisible(true);
    }
    Object localObject = (DateAxis)localXYPlot.getDomainAxis();
    //left y axis
	NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getRangeAxis();
    localNumberAxis1.setLowerMargin(0.4D);
    DecimalFormat localDecimalFormat = new DecimalFormat("00.00");
    localNumberAxis1.setNumberFormatOverride(localDecimalFormat);
    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

    return (JFreeChart)localJFreeChart;
  }

  class NiftyPCTableModel extends AbstractTableModel implements TableModelListener {
        private String[] columnNames2;
        private Vector rawdata;
        private Object[][] data;
		public  NiftyPCTableModel(){
			String[] col2 = new String[55];
			col2[0] = "id";
			col2[1] = "tradedate";
			col2[2] = "NIFTY";
			col2[3] = "NIFTYVOL";
			for(int m = 4; m < 55 ; m++) col2[m] = nifty50[m-4].substring(0, nifty50[m-4].indexOf("_"));
			this.columnNames2 = col2;
			int vector_index = 0;
            rawdata = generateNiftyPCTableDataInVector(textPchangeDateFrom.getText());
            data = new Object[noOfRowsNiftyPCTable][columnNames2.length];
            for(int r = 0; r < noOfRowsNiftyPCTable; r++) {
            	for(int c = 0; c < columnNames2.length; c++) {
					data[r][c] = rawdata.get(vector_index++);
            	}
            }
        }
        public int getColumnCount() {
            return columnNames2.length;
        }

        public int getRowCount() {
            return noOfRowsNiftyPCTable;
        }

        public String getColumnName(int col) {
            return columnNames2[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
		public void tableChanged(TableModelEvent e) {
			fireTableChanged(e);
		}
  }
  class nonNiftyPCTableModel extends AbstractTableModel implements TableModelListener {
        private String[] columnNames2;
        private Vector rawdata;
        private Object[][] data;
		public  nonNiftyPCTableModel(){
			String[] col2 = new String[nonNifty.length + 4];
			col2[0] = "id";
			col2[1] = "tradedate";
			col2[2] = "NIFTY";
			col2[3] = "NIFTYVOL";
			for(int m = 4; m < (nonNifty.length + 4); m++) col2[m] = nonNifty[m-4].substring(0, nonNifty[m-4].indexOf("_"));
			this.columnNames2 = col2;
			int vector_index = 0;
            rawdata = generateNonNiftyPCTableDataInVector(textPchangeDateFrom.getText());
            data = new Object[noOfRowsNonNiftyPCTable][columnNames2.length];
            for(int r = 0; r < noOfRowsNonNiftyPCTable; r++) {
            	for(int c = 0; c < columnNames2.length; c++) {
					data[r][c] = rawdata.get(vector_index++);
            	}
            }
        }
        public int getColumnCount() {
            return columnNames2.length;
        }

        public int getRowCount() {
            return noOfRowsNonNiftyPCTable;
        }

        public String getColumnName(int col) {
            return columnNames2[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
		public void tableChanged(TableModelEvent e) {
			fireTableChanged(e);
		}
  }
  public Vector generateNiftyPCTableDataInVector(String tradedate) {
		Vector r = new Vector();
		noOfRowsNiftyPCTable = 0;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
		    Statement statement = connection.createStatement();
		    String sql = "SELECT * FROM niftypc WHERE tradedate > " + tradedate + " ORDER BY tradedate";
		    ResultSet rs = statement.executeQuery(sql);
		    while (rs.next()) {
		        Object id1 = rs.getObject("id");
                r.add(id1);
                Object tdate1 = rs.getObject("tradedate");
                r.add(tdate1);
                double nif = rs.getDouble("nifty");
	            double data1 = Double.valueOf(nif).doubleValue();
				sumOfPchanges[0] += data1;
            	r.add(sumOfPchanges[0]);
	            double nifv = rs.getDouble("niftyvol");
				double data2 = Double.valueOf(nifv).doubleValue();
				sumOfPchanges[1] += data2;
            	r.add(sumOfPchanges[1]);
                for(int i = 0; i < nifty50.length; i++) {
                	String[] scrips = null;
                	scrips = nifty50[i].split("_");
					double eachScripPC = rs.getDouble(scrips[0]);
					double data3 = Double.valueOf(eachScripPC).doubleValue();
					sumOfPchanges[i+2] += data3;
	            	r.add(sumOfPchanges[i+2]);
                }
                noOfRowsNiftyPCTable++;
		    }
		    connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return r;
  }
  public Vector generateNonNiftyPCTableDataInVector(String tradedate) {
		Vector r = new Vector();
		noOfRowsNonNiftyPCTable = 0;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
		    Statement statement = connection.createStatement();
		    String sql = "SELECT * FROM nonniftypc WHERE tradedate > " + tradedate + " ORDER BY tradedate";
		    ResultSet rs = statement.executeQuery(sql);
		    while (rs.next()) {
		        Object id1 = rs.getObject("id");
                r.add(id1);
                Object tdate1 = rs.getObject("tradedate");
                r.add(tdate1);
                double nif = rs.getDouble("nifty");
	            double data1 = Double.valueOf(nif).doubleValue();
				sumOfPchanges2[0] += data1;
            	r.add(sumOfPchanges2[0]);
	            double nifv = rs.getDouble("niftyvol");
				double data2 = Double.valueOf(nifv).doubleValue();
				sumOfPchanges2[1] += data2;
            	r.add(sumOfPchanges2[1]);
                for(int i = 0; i < nonNifty.length; i++) {
                	String[] scrips = null;
                	scrips = nonNifty[i].split("_");
					double eachScripPC = rs.getDouble(scrips[0]);
					double data3 = Double.valueOf(eachScripPC).doubleValue();
					sumOfPchanges2[i+2] += data3;
	            	r.add(sumOfPchanges2[i+2]);
                }
                noOfRowsNonNiftyPCTable++;
		    }
		    connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return r;
  }
  class NiftyPcTableCellRenderer extends DefaultTableCellRenderer
	{
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if(column > 1) {
				Double d = Double.valueOf(value.toString()).doubleValue();
				int cellInt = d.intValue();
				if(cellInt <= 0) {
			        if(cellInt == 0) cell.setBackground(new Color(255, 255, 255));
	   		        else if(cellInt <= -1 && cellInt > -6) cell.setBackground(new Color(255, 125, 255));
	   		        else if(cellInt <= -6 && cellInt > -11) cell.setBackground(new Color(255, 115, 255));
	   		        else if(cellInt <= -11 && cellInt > -16) cell.setBackground(new Color(255, 105, 255));
	   		        else if(cellInt <= -16 && cellInt > -21) cell.setBackground(new Color(255, 95, 255));
	   		        else if(cellInt <= -21 && cellInt > -31) cell.setBackground(new Color(255, 85, 255));
	   		        else if(cellInt <= -31 && cellInt > -41) cell.setBackground(new Color(255, 75, 255));
	   		        else if(cellInt <= -41 && cellInt > -51) cell.setBackground(new Color(255, 65, 255));
	   		        else if(cellInt <= -51 && cellInt > -61) cell.setBackground(new Color(255, 55, 255));
	   		        else if(cellInt <= -61 && cellInt > -71) cell.setBackground(new Color(255, 45, 255));
	   		        else if(cellInt <= -71 && cellInt > -81) cell.setBackground(new Color(255, 35, 255));
	   		        else if(cellInt <= -81 && cellInt > -91) cell.setBackground(new Color(255, 25, 255));
	   		        else if(cellInt <= -91 && cellInt > -101) cell.setBackground(new Color(255, 15, 255));
	   		        else cell.setBackground(new Color(255, 5, 255));
	   		    } else {
	   		    	if(cellInt >= 1 && cellInt < 6) cell.setBackground(new Color(125, 255, 255));
			        else if(cellInt >= 6 && cellInt < 11) cell.setBackground(new Color(115, 255, 255));
	   		        else if(cellInt >= 11 && cellInt < 16) cell.setBackground(new Color(105, 255, 255));
	   		        else if(cellInt >= 16 && cellInt < 21) cell.setBackground(new Color(95, 255, 255));
	   		        else if(cellInt >= 21 && cellInt < 31) cell.setBackground(new Color(85, 255, 255));
	   		        else if(cellInt >= 31 && cellInt < 41) cell.setBackground(new Color(75, 255, 255));
	   		        else if(cellInt >= 41 && cellInt < 51) cell.setBackground(new Color(65, 255, 255));
	   		        else if(cellInt >= 51 && cellInt < 61) cell.setBackground(new Color(55, 255, 255));
	   		        else if(cellInt >= 61 && cellInt < 71) cell.setBackground(new Color(45, 255, 255));
	   		        else if(cellInt >= 71 && cellInt < 81) cell.setBackground(new Color(35, 255, 255));
	   		        else if(cellInt >= 81 && cellInt < 91) cell.setBackground(new Color(25, 255, 255));
	   		        else if(cellInt >= 91 && cellInt < 101) cell.setBackground(new Color(15, 255, 255));
	   		        else cell.setBackground(new Color(5, 255, 255));
	   		    }
		    } else {
		    	cell.setBackground(new Color(255, 255, 255));
		    }
	        return cell;
	    }
	}

  class MyScripsTableModel extends AbstractTableModel implements TableModelListener {
        private String[] columnNames = {"id",
										"tdate",
                                        "scrip",
                                        "ttype",
                                        "price",
                                        "quantity",
                                        "amount",
                                        "taxbrk"};
        private Vector rawdata;
        private Object[][] data;
		public  MyScripsTableModel(){
				int vector_index = 0;
                rawdata = generateMyScripsTableDataInVector();
                data = new Object[noOfRowsMyScripsTable][columnNames.length];
                for(int r = 0; r < noOfRowsMyScripsTable; r++) {
                	for(int c = 0; c < columnNames.length; c++) {
						data[r][c] = rawdata.get(vector_index++);
                	}
                }
        }
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return noOfRowsMyScripsTable;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
		public void tableChanged(TableModelEvent e) {
			fireTableChanged(e);
		}
  }
	public Vector generateMyScripsTableDataInVector() {
		Vector data = new Vector();
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + MYSCRIPS_DB, "root","deepak");
		    Statement statement = connection.createStatement();
		    String sql = "SELECT * FROM " + cboMyEquity.getSelectedItem() + " ORDER BY tdate";
		    ResultSet rs = statement.executeQuery(sql);
		    noOfRowsMyScripsTable = 0;
		    while (rs.next()) {
		        String id1 = rs.getString("id");
                data.add(id1);
                String tdate1 = rs.getString("tdate");
                data.add(tdate1);
                String scrip1 = rs.getString("scrip");
                data.add(scrip1);
                String ttype1 = rs.getString("ttype");
                data.add(ttype1);
                String price1 = rs.getString("price");
                data.add(price1);
                String quantity1 = rs.getString("quantity");
                data.add(quantity1);
                String amount1 = rs.getString("amount");
                data.add(amount1);
                String taxbrk1 = rs.getString("taxbrk");
                data.add(taxbrk1);
                noOfRowsMyScripsTable++;
		    }
		    connection.close();
		} catch(Exception e) {
		                  System.out.println(e);
		}
		return data;
	}
  public String[] getHeadersForTable(String db, String tname, String startDate, String endDate)
  {
       getSingleColumn(db, db + "_" + tname, "tradedate", " WHERE tradedate BETWEEN '" + startDate + "' AND '" + endDate +"' order by tradedate");
       String[] columnNames = new String[totalTradeDates + 1];
       columnNames[0] = "fnoScrip";
       for(int r=0; r < totalTradeDates; ++r) columnNames[r+1] = tradeDates[r];
       return   columnNames;
  }
  public Object[][] getDataForTable(String db, String tname, String startDate, String endDate)
  {
         int r = 1;
         Object[][] data = new Object[r][totalTradeDates + 1];

                 data[--r][0] = tname;
                 for(int c=0; tradeDates[c]!=null; ++c)
                 {
                         getSingleColumn(db, db + "_"  + tname, "ohlc", " WHERE tradedate BETWEEN '" + startDate + "' AND '" + endDate +"' order by tradedate");
                         String[] s = null;
                         s = closeData[c].split(",");
                         String s2 = new String(s[3]);
                         Double p = Double.valueOf(s2);
                         double res;
                         if(c == 0){refValue = p;}
                         res = (p - refValue)/(refValue/100.0);
                         if(c != 0) data[r][c+1] = String.valueOf(res).substring(0, (String.valueOf(res).indexOf('.') + 2)) + "*" + s2;
                         else  data[r][c+1] = s2;
                 }
         return  data;
  }
  public void insertWorldIndicesToDatabase(String tableName, String value)
  {
     	try
     	{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + WORLDINDICES_DB, "root","deepak");
            Statement statement = connection.createStatement();
            String myDate =new String(textDateWorldIndices.getText()), ohlcv = new String(value);

            String sql1 = "CREATE TABLE IF NOT EXISTS " + tableName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
            //System.out.println(sql1);
			statement.executeUpdate(sql1);
            String sql2 = "INSERT INTO " + tableName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + ohlcv + "')";
            //System.out.println(sql2);
			statement.executeUpdate(sql2);

         	connection.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
  }
  public void insertRbiXlsFileToDatabase(String fileName)
  {
     //Not complete
     try
     {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
            Statement statement = connection.createStatement();
            System.out.println(fileName);
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileName));
                String scripName = null, myDate = "2012-02-02";
                String usd = null, gbp = null, eur = null, jpy = null, close = null, oi = null;

				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					HSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows
							+ " row(s).");
					for (int r = 0; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}

						int cells = row.getPhysicalNumberOfCells();
						System.out.println("\nROW " + row.getRowNum() + " has " + cells
								+ " cell(s).");
						for (int c = 0; c < cells; c++) {
							HSSFCell cell = row.getCell(c);
							String value = null;

							switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                        value = (String.valueOf(cal.get(Calendar.YEAR)));
                                        value =  value + "_" + (cal.get(Calendar.MONTH)+1) + "_" + cal.get(Calendar.DAY_OF_MONTH);
                                    } else {
                                        value = "" + cell.getNumericCellValue();
                                    }
									break;
								case HSSFCell.CELL_TYPE_STRING:
									value = cell.getRichStringCellValue().getString();
									break;
								default:
							}
						System.out.println("CELL col=" + cell.getColumnIndex() + " VALUE=" + value);
                         //if(cell.getColumnIndex() == 6 ) open = value.trim();
                         //if(cell.getColumnIndex() == 7 ) high = value.trim();
                         //if(cell.getColumnIndex() == 8 ) low = value.trim();
                         //if(cell.getColumnIndex() == 9 ) close = value.trim();
                         //if(cell.getColumnIndex() == 14 ) oi = value.trim();
                         //if(cell.getColumnIndex() == 0 ) scripName = value.trim();
                         //if(cell.getColumnIndex() == 1 ) scripName = scripName + "_" + value.trim();
						}
						//System.out.println("tDate=" + myDate + " scripName=" + scripName + " ohlcv='" + open + "," + high + "," + low + "," + close + "," + oi + "'");
                        usd = new String(close.replaceAll("\"",""));
                        String sql1 = "CREATE TABLE IF NOT EXISTS Rbi_USDINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        //statement.executeUpdate(sql1);
                        String sql2 = "INSERT INTO Rbi_USDINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + usd + ","  + usd + ","  + usd + ","  + usd + ","  + oi + "')";
                         //statement.executeUpdate(sql2);
                        System.out.println(sql1);
                        System.out.println(sql2);

                        eur = new String(close.replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_EURINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        //statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_EURINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + eur + ","  + eur + ","  + eur + ","  + eur + ","  + oi + "')";
                        //statement.executeUpdate(sql2);
                        System.out.println(sql1);
                        System.out.println(sql2);

                        gbp = new String(close.replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_GBPINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        //statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_GBPINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + gbp  + ","  + gbp + ","  + gbp + ","  + gbp + ","  + oi + "')";
                        //statement.executeUpdate(sql2);
                        System.out.println(sql1);
                        System.out.println(sql2);

                        jpy = new String(close.replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_JPYINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        //statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_JPYINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + jpy + ","  + jpy + ","  + jpy + ","  + jpy + ","  + oi + "')";
                        //statement.executeUpdate(sql2);
                        System.out.println(sql1);
                        System.out.println(sql2);
					}
				}
         connection.close();
        }catch(Exception e) {
            System.out.println("Exception in POI or JDBC Sir");
            e.printStackTrace();
        }
  }
  public void insertNiftyCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 0) && (count <= 4000))
                {
                        String date1, o, h, l, c, t;
                        date1 = new String(columns[0].replaceAll("\"","").trim());;
                        String d =new String(date1.substring(0,2));
                        String m1 =new String(date1.substring(3,6).toLowerCase());
                        String m = "";
                        if(m1.equals("jan")) m = "01";
                        else if(m1.equals("jan")) m = "01";
                        else if(m1.equals("feb")) m = "02";
                        else if(m1.equals("mar")) m = "03";
                        else if(m1.equals("apr")) m = "04";
                        else if(m1.equals("may")) m = "05";
                        else if(m1.equals("jun")) m = "06";
                        else if(m1.equals("jul")) m = "07";
                        else if(m1.equals("aug")) m = "08";
                        else if(m1.equals("sep")) m = "09";
                        else if(m1.equals("oct")) m = "10";
                        else if(m1.equals("nov")) m = "11";
                        else if(m1.equals("dec")) m = "12";

                        String y =new String(date1.substring(7));
                        String myDate =  y + "-" + m + "-" + d;

                        o = new String(columns[1].replaceAll("\"","").trim());
                        h = new String(columns[2].replaceAll("\"","").trim());
                        l = new String(columns[3].replaceAll("\"","").trim());
                        c = new String(columns[4].replaceAll("\"","").trim());
                        t = new String(columns[5].replaceAll("\"","").trim());
                        String sql1 = "CREATE TABLE IF NOT EXISTS nse_nifty (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        if (h.equals(l) ) { o = c; h = c ; l = c; }
                        String sql2 = "INSERT INTO nse_nifty (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + t + "')";
                        statement.executeUpdate(sql2);
                        System.out.println("Nifty " + myDate + " Inserted");

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertBseEquityCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";

            String[] result = null;
            String d =new String(fileName.substring(2,4));
            String m =new String(fileName.substring(4,6));
            String y =new String(fileName.substring(6,8));
            y = "20" + y;
            String myDate =  y + m + d;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + BSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                result = strLine.split(",");
                if(count > 0)
                {
                        String sname,o,h,l,c,v;
                        sname = new String(result[1].replaceAll("\\W","").trim()); //+ "_" + result[0].replaceAll("\\W","").trim() + "_" + result[2].replaceAll("\\W","").trim() + "_" + result[3].replaceAll("\\W","").trim());
                        o = new String(result[4].replaceAll("\"","").trim());
                        h = new String(result[5].replaceAll("\"","").trim());
                        l = new String(result[6].replaceAll("\"","").trim());
                        c = new String(result[7].replaceAll("\"","").trim());
                        v = new String(result[11].replaceAll("\"","").trim());
                        String sql1 = "CREATE TABLE IF NOT EXISTS BSE_"+ sname + "(id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        if (h.equals(l) ) { o = c; h = c ; l = c; }
                        String sql2 = "INSERT INTO BSE_"+ sname + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + v + "')";
                        statement.executeUpdate(sql2);
                        System.out.println(sname);
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNseFnoCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSEFNO_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi, optmmm1="", optmmm2="", optmmm3="", contract1="", contract2="", contract3="";
                        date1 = new String(fileName.substring(0,12).replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        String y =new String(date1.substring(7,11));
                        int currentYear = Integer.valueOf(y), nextYear = Integer.valueOf(y);
                        if(m1.equals("jan")) {m = "01"; optmmm1 = "01"; optmmm2 = "02"; optmmm3 = "03";}
                        else if(m1.equals("feb")) {m = "02"; optmmm1 = "02"; optmmm2 = "03"; optmmm3 = "04";}
                        else if(m1.equals("mar")) {m = "03"; optmmm1 = "03"; optmmm2 = "04"; optmmm3 = "05";}
                        else if(m1.equals("apr")) {m = "04"; optmmm1 = "04"; optmmm2 = "05"; optmmm3 = "06";}
                        else if(m1.equals("may")) {m = "05"; optmmm1 = "05"; optmmm2 = "06"; optmmm3 = "07";}
                        else if(m1.equals("jun")) {m = "06"; optmmm1 = "06"; optmmm2 = "07"; optmmm3 = "08";}
                        else if(m1.equals("jul")) {m = "07"; optmmm1 = "07"; optmmm2 = "08"; optmmm3 = "09";}
                        else if(m1.equals("aug")) {m = "08"; optmmm1 = "08"; optmmm2 = "09"; optmmm3 = "10";}
                        else if(m1.equals("sep")) {m = "09"; optmmm1 = "09"; optmmm2 = "10"; optmmm3 = "11";}
                        else if(m1.equals("oct")) {m = "10"; optmmm1 = "10"; optmmm2 = "11"; optmmm3 = "12";}
                        else if(m1.equals("nov")) {m = "11"; optmmm1 = "11"; optmmm2 = "12"; optmmm3 = "01"; ++nextYear;}
                        else if(m1.equals("dec")) {m = "12"; optmmm1 = "12"; optmmm2 = "01"; optmmm3 = "02"; ++nextYear;}

                        String myDate =  y + "-" + m + "-" + d;
                        if(optmmm1.equals("11"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("12"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(nextYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("01") || optmmm1.equals("02") || optmmm1.equals("03") ||  optmmm1.equals("04") ||  optmmm1.equals("05") ||  optmmm1.equals("06") ||  optmmm1.equals("07") || optmmm1.equals("08") ||  optmmm1.equals("09") ||  optmmm1.equals("10"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(currentYear) + "_" + optmmm3;
                        }
                        o = new String(columns[5].replaceAll("\"","").trim());
                        h = new String(columns[6].replaceAll("\"","").trim());
                        l = new String(columns[7].replaceAll("\"","").trim());
                        c = new String(columns[8].replaceAll("\"","").trim());
                        oi = new String(columns[12].replaceAll("\"","").trim());

                        String cy = null, cm = null, cmt = null, cd = null, temp = columns[2].replaceAll("\\W","").toLowerCase();
                        cy =  temp.substring(5);
                        cmt =  temp.substring(2,5);

                        if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("feb")) cm = "02";
                        else if(cmt.equals("mar")) cm = "03";
                        else if(cmt.equals("apr")) cm = "04";
                        else if(cmt.equals("may")) cm = "05";
                        else if(cmt.equals("jun")) cm = "06";
                        else if(cmt.equals("jul")) cm = "07";
                        else if(cmt.equals("aug")) cm = "08";
                        else if(cmt.equals("sep")) cm = "09";
                        else if(cmt.equals("oct")) cm = "10";
                        else if(cmt.equals("nov")) cm = "11";
                        else if(cmt.equals("dec")) cm = "12";

                        cd =  temp.substring(0,2);

                        String scripName;
                        if(columns[0].substring(0,1).equals("F"))
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd );
                        }
                        else
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd + "_" + (columns[3].replaceAll("\\W","")) + "_" + (columns[4].replaceAll("\\W","")));
                        }
                        if(columns[0].substring(0,1).equals("F") || ( scripName.substring(0, 7).equalsIgnoreCase("O_NIFTY") && (scripName.substring(8,15).equals(contract1) || scripName.substring(8,15).equals(contract2) || scripName.substring(8,15).equals(contract3)) ) )
                        {

                         String sql1 = "CREATE TABLE IF NOT EXISTS nsefno_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nsefno_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                        }
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNseOptCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSEOPT_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi, optmmm1="", optmmm2="", optmmm3="", contract1="", contract2="", contract3="";
                        date1 = new String(fileName.substring(0,12).replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        String y =new String(date1.substring(7,11));
                        int currentYear = Integer.valueOf(y), nextYear = Integer.valueOf(y);
                        if(m1.equals("jan")) {m = "01"; optmmm1 = "01"; optmmm2 = "02"; optmmm3 = "03";}
                        else if(m1.equals("feb")) {m = "02"; optmmm1 = "02"; optmmm2 = "03"; optmmm3 = "04";}
                        else if(m1.equals("mar")) {m = "03"; optmmm1 = "03"; optmmm2 = "04"; optmmm3 = "05";}
                        else if(m1.equals("apr")) {m = "04"; optmmm1 = "04"; optmmm2 = "05"; optmmm3 = "06";}
                        else if(m1.equals("may")) {m = "05"; optmmm1 = "05"; optmmm2 = "06"; optmmm3 = "07";}
                        else if(m1.equals("jun")) {m = "06"; optmmm1 = "06"; optmmm2 = "07"; optmmm3 = "08";}
                        else if(m1.equals("jul")) {m = "07"; optmmm1 = "07"; optmmm2 = "08"; optmmm3 = "09";}
                        else if(m1.equals("aug")) {m = "08"; optmmm1 = "08"; optmmm2 = "09"; optmmm3 = "10";}
                        else if(m1.equals("sep")) {m = "09"; optmmm1 = "09"; optmmm2 = "10"; optmmm3 = "11";}
                        else if(m1.equals("oct")) {m = "10"; optmmm1 = "10"; optmmm2 = "11"; optmmm3 = "12";}
                        else if(m1.equals("nov")) {m = "11"; optmmm1 = "11"; optmmm2 = "12"; optmmm3 = "01"; ++nextYear;}
                        else if(m1.equals("dec")) {m = "12"; optmmm1 = "12"; optmmm2 = "01"; optmmm3 = "02"; ++nextYear;}

                        String myDate =  y + "-" + m + "-" + d;
                        if(optmmm1.equals("11"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("12"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(nextYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("01") || optmmm1.equals("02") || optmmm1.equals("03") ||  optmmm1.equals("04") ||  optmmm1.equals("05") ||  optmmm1.equals("06") ||  optmmm1.equals("07") || optmmm1.equals("08") ||  optmmm1.equals("09") ||  optmmm1.equals("10"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(currentYear) + "_" + optmmm3;
                        }
                        o = new String(columns[5].replaceAll("\"","").trim());
                        h = new String(columns[6].replaceAll("\"","").trim());
                        l = new String(columns[7].replaceAll("\"","").trim());
                        c = new String(columns[8].replaceAll("\"","").trim());
                        oi = new String(columns[12].replaceAll("\"","").trim());

                        String cy = null, cm = null, cmt = null, cd = null, temp = columns[2].replaceAll("\\W","").toLowerCase();
                        cy =  temp.substring(5);
                        cmt =  temp.substring(2,5);

                        if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("feb")) cm = "02";
                        else if(cmt.equals("mar")) cm = "03";
                        else if(cmt.equals("apr")) cm = "04";
                        else if(cmt.equals("may")) cm = "05";
                        else if(cmt.equals("jun")) cm = "06";
                        else if(cmt.equals("jul")) cm = "07";
                        else if(cmt.equals("aug")) cm = "08";
                        else if(cmt.equals("sep")) cm = "09";
                        else if(cmt.equals("oct")) cm = "10";
                        else if(cmt.equals("nov")) cm = "11";
                        else if(cmt.equals("dec")) cm = "12";

                        cd =  temp.substring(0,2);

                        String scripName;
                        if(columns[0].substring(0,1).equals("F"))
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd );
                        }
                        else
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd + "_" + (columns[3].replaceAll("\\W","")) + "_" + (columns[4].replaceAll("\\W","")));
                        }
                        if(!(oi.equals("0")) && columns[0].substring(0,1).equals("O") /*&& !( scripName.substring(0, 7).equalsIgnoreCase("O_NIFTY"))*/ )
                        {

                         String sql1 = "CREATE TABLE IF NOT EXISTS nseopt_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         //System.out.println(sql1);
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nseopt_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         //System.out.println(sql2);
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                        }
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertRbiCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + RBI_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 0) && (count <= 4000))
                {
                        String date1, usd, eur, gbp, jpy, oi;
                        date1 = new String(columns[0].replaceAll("\\W",""));;
                        String d =new String(date1.substring(0,2));
                        String m1 =new String(date1.substring(2,5).toLowerCase());
                        String m = "";
                        if(m1.equals("jan")) m = "01";
                        else if(m1.equals("jan")) m = "01";
                        else if(m1.equals("feb")) m = "02";
                        else if(m1.equals("mar")) m = "03";
                        else if(m1.equals("apr")) m = "04";
                        else if(m1.equals("may")) m = "05";
                        else if(m1.equals("jun")) m = "06";
                        else if(m1.equals("jul")) m = "07";
                        else if(m1.equals("aug")) m = "08";
                        else if(m1.equals("sep")) m = "09";
                        else if(m1.equals("oct")) m = "10";
                        else if(m1.equals("nov")) m = "11";
                        else if(m1.equals("dec")) m = "12";

                        String y =new String(date1.substring(5,7));
                        String myDate =  "20" + y + "-" + m + "-" + d;
                        oi = new String(columns[4].replaceAll("\"",""));

                        usd = new String(columns[5].replaceAll("\"",""));
                        String sql1 = "CREATE TABLE IF NOT EXISTS Rbi_USDINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        String sql2 = "INSERT INTO Rbi_USDINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + usd + ","  + usd + ","  + usd + ","  + usd + ","  + oi + "')";
                         statement.executeUpdate(sql2);

                        eur = new String(columns[6].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_EURINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_EURINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + eur + ","  + eur + ","  + eur + ","  + eur + ","  + oi + "')";
                        statement.executeUpdate(sql2);

                        gbp = new String(columns[7].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_GBPINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_GBPINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + gbp  + ","  + gbp + ","  + gbp + ","  + gbp + ","  + oi + "')";
                        statement.executeUpdate(sql2);

                        jpy = new String(columns[8].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_JPYINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_JPYINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + jpy + ","  + jpy + ","  + jpy + ","  + jpy + ","  + oi + "')";
                        statement.executeUpdate(sql2);
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
            e.printStackTrace();
        }
  }
  public void insertRbiCsvFileFromRbiSiteToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + RBI_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 0) && (count <= 4000))
                {
                        String date1, usd, eur, gbp, jpy, oi;
                        date1 = new String(columns[0].replaceAll("\\W",""));
                        String d =new String(date1.substring(0,2));
                        String m =new String(date1.substring(2,4));
                         String y =new String(date1.substring(4,8));
                        String myDate =  y + "-" + m + "-" + d;
                        oi = "0";
                        System.out.println(myDate);

                        usd = new String(columns[1].replaceAll("\"",""));
                        String sql1 = "CREATE TABLE IF NOT EXISTS Rbi_USDINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        String sql2 = "INSERT INTO Rbi_USDINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + usd + ","  + usd + ","  + usd + ","  + usd + ","  + oi + "')";
                         statement.executeUpdate(sql2);

                        eur = new String(columns[3].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_EURINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_EURINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + eur + ","  + eur + ","  + eur + ","  + eur + ","  + oi + "')";
                        statement.executeUpdate(sql2);

                        gbp = new String(columns[2].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_GBPINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_GBPINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + gbp  + ","  + gbp + ","  + gbp + ","  + gbp + ","  + oi + "')";
                        statement.executeUpdate(sql2);

                        jpy = new String(columns[4].replaceAll("\"",""));
                        sql1 = "CREATE TABLE IF NOT EXISTS Rbi_JPYINR (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        sql2 = "INSERT INTO Rbi_JPYINR (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + jpy + ","  + jpy + ","  + jpy + ","  + jpy + ","  + oi + "')";
                        statement.executeUpdate(sql2);

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
            e.printStackTrace();
        }
  }
  public void insertNseEquityCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi;
                        date1 = new String(fileName.replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        if(m1.equals("jan")) m = "01";
                        else if(m1.equals("jan")) m = "01";
                        else if(m1.equals("feb")) m = "02";
                        else if(m1.equals("mar")) m = "03";
                        else if(m1.equals("apr")) m = "04";
                        else if(m1.equals("may")) m = "05";
                        else if(m1.equals("jun")) m = "06";
                        else if(m1.equals("jul")) m = "07";
                        else if(m1.equals("aug")) m = "08";
                        else if(m1.equals("sep")) m = "09";
                        else if(m1.equals("oct")) m = "10";
                        else if(m1.equals("nov")) m = "11";
                        else if(m1.equals("dec")) m = "12";

                        String y =new String(date1.substring(7,11));
                        String myDate =  y + "-" + m + "-" + d;

                        o = new String(columns[2].replaceAll("\"","").trim());
                        h = new String(columns[3].replaceAll("\"","").trim());
                        l = new String(columns[4].replaceAll("\"","").trim());
                        c = new String(columns[5].replaceAll("\"","").trim());
                        oi = new String(columns[8].replaceAll("\"","").trim());
                        String scripName = new  String((columns[0].replaceAll("\\W","")) + "_" + (columns[1].replaceAll("\\W","")) );

                         String sql1 = "CREATE TABLE IF NOT EXISTS nse_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nse_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertMcxFutCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + MCX_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 0) && (count <= 4000))
                {
                        String date, o, h, l, c, oi;
                        date = new String(fileName.substring(0,8).replaceAll("\"","").trim());;
                        String d =new String(date.substring(0,2));
                        String m =new String(date.substring(2,4));
                        String y =new String(date.substring(4));
                        String myDate =  y + "-" + m + "-" + d;

                        o = new String(columns[3].replaceAll("\"","").trim());
                        h = new String(columns[4].replaceAll("\"","").trim());
                        l = new String(columns[5].replaceAll("\"","").trim());
                        c = new String(columns[6].replaceAll("\"","").trim());
                        oi = new String(columns[11].replaceAll("\"","").trim());
                        String cy = null, cm = null, cmt = null, cd = null, temp = columns[2].replaceAll("\\W","").toLowerCase();
                        //System.out.println(temp);
                        cy =  temp.substring(5);
                        cmt =  temp.substring(2,5);

                        if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("feb")) cm = "02";
                        else if(cmt.equals("mar")) cm = "03";
                        else if(cmt.equals("apr")) cm = "04";
                        else if(cmt.equals("may")) cm = "05";
                        else if(cmt.equals("jun")) cm = "06";
                        else if(cmt.equals("jul")) cm = "07";
                        else if(cmt.equals("aug")) cm = "08";
                        else if(cmt.equals("sep")) cm = "09";
                        else if(cmt.equals("oct")) cm = "10";
                        else if(cmt.equals("nov")) cm = "11";
                        else if(cmt.equals("dec")) cm = "12";

                        cd =  temp.substring(0,2);
                        String scripName = new  String(columns[1].replaceAll("\\W","") + "_" + cy + "_" + cm + "_" + cd) ;
                         String sql1 = "CREATE TABLE IF NOT EXISTS mcx_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO mcx_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNcdexFutXlsFileToDatabase(String fileName)
  {
     try
     {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + FIIDII_DB, "root","deepak");
            Statement statement = connection.createStatement();
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileName));
                String fileNameDate = new String(fileName.replaceAll("\\W","").substring(0,9));;
                String m =new String(fileNameDate.substring(0,2));
                String d =new String(fileNameDate.substring(2,4).toLowerCase());
                String y =new String(fileNameDate.substring(4, 8));
                String myDate =  y + "-" + m + "-" + d;
                String scripName = null, contract_month = null;
                String open = null, high = null, low = null, close = null, oi = null;

				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					HSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows
					//		+ " row(s).");
					for (int r = 2; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}

						int cells = row.getPhysicalNumberOfCells();
						//System.out.println("\nROW " + row.getRowNum() + " has " + cells
						//		+ " cell(s).");
						for (int c = 0; c < cells; c++) {
							HSSFCell cell = row.getCell(c);
							String value = null;

							switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                        value = (String.valueOf(cal.get(Calendar.YEAR)));
                                        value =  value + "_" + (cal.get(Calendar.MONTH)+1) + "_" + cal.get(Calendar.DAY_OF_MONTH);
                                    } else {
                                        value = "" + cell.getNumericCellValue();
                                    }
									break;
								case HSSFCell.CELL_TYPE_STRING:
									value = cell.getRichStringCellValue().getString();
									break;
								default:
							}
						//System.out.println("CELL col=" + cell.getColumnIndex() + " VALUE=" + value);
                         if(cell.getColumnIndex() == 6 ) open = value.trim();
                         if(cell.getColumnIndex() == 7 ) high = value.trim();
                         if(cell.getColumnIndex() == 8 ) low = value.trim();
                         if(cell.getColumnIndex() == 9 ) close = value.trim();
                         if(cell.getColumnIndex() == 14 ) oi = value.trim();
                         if(cell.getColumnIndex() == 0 ) scripName = value.trim();
                         if(cell.getColumnIndex() == 1 ) scripName = scripName + "_" + value.trim();
						}
						//System.out.println("tDate=" + myDate + " scripName=" + scripName + " ohlcv='" + open + "," + high + "," + low + "," + close + "," + oi + "'");
                         String sql1 = "CREATE TABLE IF NOT EXISTS ncdex_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         //System.out.println(sql1);
                         if (high.equals(low) ) { open = close; high = close ; low = close; }
                         String sql2 = "INSERT INTO ncdex_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + open + "," + high + "," + low + "," + close + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         //System.out.println(sql2);
                         System.out.println(scripName + " Inserted");
					}
				}
         connection.close();
        }catch(Exception e) {
            System.out.println("Exception JDBC: " + e);
        }
  }
  public void insertNcdexFutCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NCDEX_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 1) && (count <= 4000))
                {
                        String date1, o, h, l, c, oi;
                        date1 = new String(fileName.replaceAll("\\W","").substring(0,9));;
                        String m =new String(date1.substring(0,2));
                        String d =new String(date1.substring(2,4).toLowerCase());
                        String y =new String(date1.substring(4, 8));
                        String myDate =  y + "-" + m + "-" + d;
                        o = new String(columns[6].replaceAll("\"","").trim());
                        h = new String(columns[7].replaceAll("\"","").trim());
                        l = new String(columns[8].replaceAll("\"","").trim());
                        c = new String(columns[9].replaceAll("\"","").trim());
                        oi = new String(columns[14].replaceAll("\"","").trim());

                        String[] contract = columns[1].split("-");
                        //System.out.println(contract[2].replaceAll("\\W","") + " " + contract[1].substring(0,3) + " " + contract[0].replaceAll("\\W",""));
                        String contract_month = null;
                        if(contract[1].substring(0,3).equalsIgnoreCase("jan")) contract_month = "1";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("feb")) contract_month = "2";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("mar")) contract_month = "3";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("apr")) contract_month = "4";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("may")) contract_month = "5";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("jun")) contract_month = "6";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("jul")) contract_month = "7";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("aug")) contract_month = "8";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("sep")) contract_month = "9";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("oct")) contract_month = "10";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("nov")) contract_month = "11";
                        else if(contract[1].substring(0,3).equalsIgnoreCase("dec")) contract_month = "12";

                        String scripName = new  String(columns[0].replaceAll("\\W","") + "_" + contract[2].replaceAll("\\W","") + "_" + contract_month + "_" + contract[0].replaceAll("\\W",""));

                         String sql1 = "CREATE TABLE IF NOT EXISTS ncdex_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";

                         statement.executeUpdate(sql1);
                         //System.out.println(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO ncdex_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         //System.out.println(sql2);
                         System.out.println(scripName);

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNcdexFutCsvFileOldToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NCDEX_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 1) && (count <= 4000))
                {
                        String date1, o, h, l, c, oi;
                        String[] cdate;
                        date1 = new String(fileName.replaceAll("\\W","").substring(0,9));;
                        String m =new String(date1.substring(0,2));
                        String d =new String(date1.substring(2,4).toLowerCase());
                        String y =new String(date1.substring(4, 8));
                        String myDate =  y + "-" + m + "-" + d;
                        cdate = columns[1].split("/");
                        o = new String(columns[6].replaceAll("\"","").trim());
                        h = new String(columns[7].replaceAll("\"","").trim());
                        l = new String(columns[8].replaceAll("\"","").trim());
                        c = new String(columns[9].replaceAll("\"","").trim());
                        oi = new String(columns[14].replaceAll("\"","").trim());

                        String scripName = new  String(columns[0].replaceAll("\\W","") + "_" + cdate[2].replaceAll("\\W","") + "_" + cdate[0].replaceAll("\\W","") + "_" + cdate[1].replaceAll("\\W",""));
                         String sql1 = "CREATE TABLE IF NOT EXISTS ncdex_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         //System.out.println(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO ncdex_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         //System.out.println(sql2);
                         System.out.println(scripName + " Inserted");

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public static void getSingleColumn(String database, String tableName, String column, String condition)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalTradeDates = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT " + column + " FROM " + tableName + condition;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
              if(column.equalsIgnoreCase("tradedate"))
              {
                          String date = rs.getString(column.replaceAll("\"","").trim());
                          tradeDates[totalTradeDates] = date;
                          ++totalTradeDates;
               }
               else
               {
                          String ohlcdata = rs.getString(column.replaceAll("\"","").trim());
                          closeData[totalTradeDates] = ohlcdata;
                          ++totalTradeDates;
               }
        }
        connection.clearWarnings();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
  }
  public static Date createDate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    calendar.clear();
    calendar.set(paramInt1, paramInt2 - 1, paramInt3, paramInt4, paramInt5);
    return calendar.getTime();
  }
  public static OHLCDataset createDataset()
  {
    Date[] tradeDate = new Date[totalRecords];
    double[] open = new double[totalRecords];
    double[] high = new double[totalRecords];
    double[] low = new double[totalRecords];
    double[] close = new double[totalRecords];
    double[] volume = new double[totalRecords];
    for( int i = 0; i < totalRecords; i++) {
         String[] s = null;
         s = dates[i].split("-");
         int y = Integer.valueOf(s[0]);
         int m = Integer.valueOf(s[1]);
         int d = Integer.valueOf(s[2]);
         tradeDate[i] = createDate(y, m, d, 12, 0);
         s = ohlc[i].split(",");
         double o = (Double.valueOf(s[0] )).doubleValue();
         double h = (Double.valueOf(s[1] )).doubleValue();
         double l = (Double.valueOf(s[2] )).doubleValue();
         double c = (Double.valueOf(s[3] )).doubleValue();
         double v = (Double.valueOf(s[4] )).doubleValue();
         open[i] = o;
         high[i] = h;
         low[i] = l;
         close[i] = c;
         volume[i] = v;
    }

    return new DefaultHighLowDataset("", tradeDate, high, low, open, close, volume);
  }
  public static JPanel createRbiChartPanel()
  {
    JFreeChart localJFreeChart = createRbiChart(createRbiDataset());
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);
    return localChartPanel;
  }
  public static XYDataset createRbiDataset()
  {
    int[] days = new int[totalRecords];
    int[] months = new int[totalRecords];
    int[] years = new int[totalRecords];
    double[] val = new double[totalRecords];
    TimeSeries localTimeSeries1 = new TimeSeries("Price");
    for(int i = 0; ohlc[i]!=null ; ++i){

            days[i] = Integer.valueOf(new String(dates[i]).substring(8));
            months[i] = Integer.valueOf(new String(dates[i]).substring(5,7));
            years[i] = Integer.valueOf(new String(dates[i]).substring(0,4));
            String[] data = null;
            data = ohlc[i].split(",");
            val[i] = (Double.valueOf(data[0].replaceAll("\"",""))).doubleValue();
            localTimeSeries1.add(new Day(days[i], months[i],years[i]), val[i]);

    }
    TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
    localTimeSeriesCollection.addSeries(localTimeSeries1);

    return localTimeSeriesCollection;
  }

  public static JFreeChart createRbiChart(XYDataset paramXYDataset)
  {
    StandardChartTheme theme = new  StandardChartTheme("deepak2");
        theme.setTitlePaint(Color.BLUE);
        theme.setSubtitlePaint(Color.BLUE);
        theme.setChartBackgroundPaint(new Color(255, 255, 0, 0));
        theme.setPlotBackgroundPaint(new Color(255, 0, 255, 0));
        theme.setAxisLabelPaint(Color.BLACK);
        theme.setItemLabelPaint(Color.BLACK);
    ChartFactory.setChartTheme(theme);
    JFreeChart localJFreeChart = ChartFactory.createTimeSeriesChart("", "Date", "Price Per Unit", paramXYDataset, true, true, false);
    XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
    localXYPlot.setDomainPannable(true);
    localXYPlot.setRangePannable(true);
    localXYPlot.setDomainCrosshairVisible(true);
    localXYPlot.setRangeCrosshairVisible(true);
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    if ((localXYItemRenderer instanceof XYLineAndShapeRenderer))
    {
      Object localObject = (XYLineAndShapeRenderer)localXYItemRenderer;
      ((XYLineAndShapeRenderer)localObject).setSeriesShape(0, new Ellipse2D.Double(-3.0D, -3.0D, 6.0D, 6.0D));
      ((XYLineAndShapeRenderer)localObject).setDrawOutlines(true);
      ((XYLineAndShapeRenderer)localObject).setUseFillPaint(true);
      ((XYLineAndShapeRenderer)localObject).setBaseFillPaint(Color.yellow);
      ((XYLineAndShapeRenderer)localObject).setSeriesStroke(0, new BasicStroke(1.0F));
      ((XYLineAndShapeRenderer)localObject).setSeriesOutlineStroke(0, new BasicStroke(1.0F));
      ((XYLineAndShapeRenderer)localObject).setBaseShapesVisible(true);
    }
    Object localObject = (DateAxis)localXYPlot.getDomainAxis();
    NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getRangeAxis();
    localNumberAxis1.setLowerMargin(0.4D);
    DecimalFormat localDecimalFormat = new DecimalFormat("00.00");
    localNumberAxis1.setNumberFormatOverride(localDecimalFormat);
    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
    NumberAxis localNumberAxis2 = new NumberAxis("Volume");
    localNumberAxis2.setUpperMargin(1.0D);
    localXYPlot.setRangeAxis(1, localNumberAxis2);
    localXYPlot.setDataset(1, createVolumeDataset());
    localXYPlot.setRangeAxis(1, localNumberAxis2);
    localXYPlot.mapDatasetToRangeAxis(1, 1);
    XYBarRenderer localXYBarRenderer = new XYBarRenderer(0.2D);
    localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0,000.00")));
    localXYPlot.setRenderer(1, localXYBarRenderer);
    localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
    localXYBarRenderer.setShadowVisible(false);
    return (JFreeChart)localJFreeChart;
  }
  public static IntervalXYDataset createVolumeDataset()
  {
    int[] days = new int[totalRecords];
    int[] months = new int[totalRecords];
    int[] years = new int[totalRecords];
    double[] val = new double[totalRecords];
    TimeSeries localTimeSeries1 = new TimeSeries("Volume");
    for(int i = 0; ohlc[i]!=null ; ++i){

            days[i] = Integer.valueOf(new String(dates[i]).substring(8));
            months[i] = Integer.valueOf(new String(dates[i]).substring(5,7));
            years[i] = Integer.valueOf(new String(dates[i]).substring(0,4));
            String[] data = null;
            data = ohlc[i].split(",");
            val[i] = (Double.valueOf(data[4].replaceAll("\"",""))).doubleValue();
            localTimeSeries1.add(new Day(days[i], months[i],years[i]), val[i]);

    }
    TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
    localTimeSeriesCollection.addSeries(localTimeSeries1);

    return localTimeSeriesCollection;
  }
  private void copyToClipboard()
  {
    JFreeChart localJFreeChart = null;
    int i = 0;
    int j = 0;
    Component localComponent = this.chartContainerPanel.getComponent(0);
    Object localObject;
    if ((localComponent instanceof ChartPanel))
    {
      localObject = (ChartPanel)localComponent;
      localJFreeChart = ((ChartPanel)localObject).getChart();
      i = ((ChartPanel)localObject).getWidth();
      j = ((ChartPanel)localObject).getHeight();
    }
    else if ((localComponent instanceof MyChartPanel))
    {
      localObject = (MyChartPanel)localComponent;
      localJFreeChart = (JFreeChart)((MyChartPanel)localObject).charts.get(0);
      i = ((MyChartPanel)localObject).getWidth();
      j = ((MyChartPanel)localObject).getHeight();
    }
    if (localJFreeChart != null)
    {
      localObject = Toolkit.getDefaultToolkit().getSystemClipboard();
      ChartTransferable localChartTransferable = new ChartTransferable(localJFreeChart, i, j);
      ((Clipboard)localObject).setContents(localChartTransferable, null);
    }
  }
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("EXPORT_TO_PDF"))
    {
      exportToPDF();
    }
    else if (str.equals("COPY"))
    {
      copyToClipboard();
    }
    else if (str.equals("LEGACY_THEME"))
    {
      ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
      applyThemeToChart();
    }
    else if (str.equals("JFREE_THEME"))
    {
      ChartFactory.setChartTheme(StandardChartTheme.createJFreeTheme());
      applyThemeToChart();
    }
    else if (str.equals("DARKNESS_THEME"))
    {
      ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
      applyThemeToChart();
    }
    else if (str.equals("EXIT"))
    {
      attemptExit();
    }
  }
  private void applyThemeToChart()
  {
    Component localComponent = this.chartContainerPanel.getComponent(0);
    Object localObject;
    if ((localComponent instanceof ChartPanel))
    {
      localObject = (ChartPanel)localComponent;
      ChartUtilities.applyCurrentTheme(((ChartPanel)localObject).getChart());
    }
    else if ((localComponent instanceof MyChartPanel))
    {
      localObject = (MyChartPanel)localComponent;
      JFreeChart[] arrayOfJFreeChart = ((MyChartPanel)localObject).getCharts();
      for (int i = 0; i < arrayOfJFreeChart.length; i++)
        ChartUtilities.applyCurrentTheme(arrayOfJFreeChart[i]);
    }
  }
  private void exportToPDF()
  {
    JFreeChart localJFreeChart1 = null;
    int i = 0;
    int j = 0;
    Component localComponent = this.chartContainerPanel.getComponent(0);
    Object localObject;
    if ((localComponent instanceof ChartPanel))
    {
      localObject = (ChartPanel)localComponent;
      localJFreeChart1 = ((ChartPanel)localObject).getChart();
      i = ((ChartPanel)localObject).getWidth();
      j = ((ChartPanel)localObject).getHeight();
    }
    else if ((localComponent instanceof MyChartPanel))
    {
      localObject = (MyChartPanel)localComponent;
      localJFreeChart1 = (JFreeChart)((MyChartPanel)localObject).charts.get(0);
      i = ((MyChartPanel)localObject).getWidth();
      j = ((MyChartPanel)localObject).getHeight();
    }
    if (localJFreeChart1 != null)
    {
      localObject = new JFileChooser();
      ((JFileChooser)localObject).setName("untitled.pdf");
      ((JFileChooser)localObject).setFileFilter(new FileFilter()
      {
        public boolean accept(File paramFile)
        {
          return (paramFile.isDirectory()) || (paramFile.getName().endsWith(".pdf"));
        }

        public String getDescription()
        {
          return "Portable Document Format (PDF)";
        }
      });
      int k = ((JFileChooser)localObject).showSaveDialog(this);
      if (k == 0)
        try
        {
          JFreeChart localJFreeChart2 = (JFreeChart)localJFreeChart1.clone();
          PDFExportTask localPDFExportTask = new PDFExportTask(localJFreeChart2, i, j, ((JFileChooser)localObject).getSelectedFile());
          Thread localThread = new Thread(localPDFExportTask);
          localThread.start();
        }
        catch (CloneNotSupportedException localCloneNotSupportedException)
        {
          localCloneNotSupportedException.printStackTrace();
        }
    }
    else
    {
      localObject = "Unable to export the selected item.  There is ";
      localObject = (String)localObject + "either no chart selected,\nor else the chart is not ";
      localObject = (String)localObject + "at the expected location in the component hierarchy\n";
      localObject = (String)localObject + "(future versions of the demo may include code to ";
      localObject = (String)localObject + "handle these special cases).";
      JOptionPane.showMessageDialog(this, localObject, "PDF Export", 1);
    }
  }
  public static void writeChartAsPDF(OutputStream paramOutputStream, JFreeChart paramJFreeChart, int paramInt1, int paramInt2, FontMapper paramFontMapper)
    throws IOException
  {
    Rectangle localRectangle = new Rectangle(paramInt1, paramInt2);
    Document localDocument = new Document(localRectangle, 50.0F, 50.0F, 50.0F, 50.0F);
    try
    {
      PdfWriter localPdfWriter = PdfWriter.getInstance(localDocument, paramOutputStream);
      localDocument.addAuthor("deepak");
      localDocument.addSubject("moneyTree");
      localDocument.open();
      PdfContentByte localPdfContentByte = localPdfWriter.getDirectContent();
      PdfTemplate localPdfTemplate = localPdfContentByte.createTemplate(paramInt1, paramInt2);
      Graphics2D localGraphics2D = localPdfTemplate.createGraphics(paramInt1, paramInt2, paramFontMapper);
      Rectangle2D.Double localDouble = new Rectangle2D.Double(0.0D, 0.0D, paramInt1, paramInt2);
      paramJFreeChart.draw(localGraphics2D, localDouble);
      localGraphics2D.dispose();
      localPdfContentByte.addTemplate(localPdfTemplate, 0.0F, 0.0F);
    }
    catch (DocumentException localDocumentException)
    {
      System.err.println(localDocumentException.getMessage());
    }
    localDocument.close();
  }
  public static void saveChartAsPDF(File paramFile, JFreeChart paramJFreeChart, int paramInt1, int paramInt2, FontMapper paramFontMapper)
    throws IOException
  {
    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile));
    writeChartAsPDF(localBufferedOutputStream, paramJFreeChart, paramInt1, paramInt2, paramFontMapper);
    localBufferedOutputStream.close();
  }
	static class PDFExportTask implements Runnable
	{
	    JFreeChart chart;
	    int width;
	    int height;
	    File file;

	    public PDFExportTask(JFreeChart paramJFreeChart, int paramInt1, int paramInt2, File paramFile)
	    {
	      this.chart = paramJFreeChart;
	      this.file = paramFile;
	      this.width = paramInt1;
	      this.height = paramInt2;
	      paramJFreeChart.setBorderVisible(true);
	      paramJFreeChart.setPadding(new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D));
	    }

	    public void run()
	    {
	      try
	      {
	        MoneyTree.saveChartAsPDF(this.file, this.chart, this.width, this.height, new DefaultFontMapper());
	      }
	      catch (IOException localIOException)
	      {
	        localIOException.printStackTrace();
	      }
	    }
	}
	private void attemptExit()
	{
		String str1 = "Confirm";
		String str2 = "Are you sure you want to exit?";
		int i = JOptionPane.showConfirmDialog(this, str2, str1, 0, 3);
		if (i == 0)
		{
		  dispose();
		  System.exit(0);
		}
	}
	public void createConnectionDialog() {
		userNameLabel = new JLabel("User name: ", JLabel.RIGHT);
		userNameField = new JTextField("root");
		passwordLabel = new JLabel("Password: ", JLabel.RIGHT);
		passwordField = new JTextField("deepak");
		serverLabel = new JLabel("Database URL: ", JLabel.RIGHT);
		serverField = new JTextField("jdbc:mysql://localhost/" + BSE_DB);
		driverLabel = new JLabel("Driver: ", JLabel.RIGHT);
		driverField = new JTextField("com.mysql.jdbc.Driver");
		connectionPanel = new JPanel(false);
		connectionPanel.setLayout(new BoxLayout(connectionPanel,
		BoxLayout.X_AXIS));
		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));
		mainPanel.setPreferredSize(new Dimension(1024, 600));
		namePanel.add(userNameLabel);
		namePanel.add(passwordLabel);
		namePanel.add(serverLabel);
		namePanel.add(driverLabel);
		JPanel fieldPanel = new JPanel(false);
		fieldPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.add(userNameField);
		fieldPanel.add(passwordField);
		fieldPanel.add(serverField);
		fieldPanel.add(driverField);
		connectionPanel.add(namePanel);
		connectionPanel.add(fieldPanel);
    }
	public void createConnectionDialog2() {
		userNameLabel2 = new JLabel("User name: ", JLabel.RIGHT);
		userNameField2 = new JTextField("root");
		passwordLabel2 = new JLabel("Password: ", JLabel.RIGHT);
		passwordField2 = new JTextField("deepak");
		serverLabel2 = new JLabel("Database URL: ", JLabel.RIGHT);
		serverField2 = new JTextField("jdbc:mysql://localhost/" + NSE_DB);
		driverLabel2 = new JLabel("Driver: ", JLabel.RIGHT);
		driverField2 = new JTextField("com.mysql.jdbc.Driver");
		connectionPanel2 = new JPanel(false);
		connectionPanel2.setLayout(new BoxLayout(connectionPanel2, BoxLayout.X_AXIS));
		JPanel namePanel2 = new JPanel(false);
		namePanel2.setLayout(new GridLayout(0, 1));
		mainPanel2.setPreferredSize(new Dimension(1024, 600));
		namePanel2.add(userNameLabel2);
		namePanel2.add(passwordLabel2);
		namePanel2.add(serverLabel2);
		namePanel2.add(driverLabel2);
		JPanel fieldPanel2 = new JPanel(false);
		fieldPanel2.setLayout(new GridLayout(0, 1));
		fieldPanel2.add(userNameField2);
		fieldPanel2.add(passwordField2);
		fieldPanel2.add(serverField2);
	    fieldPanel2.add(driverField2);
		connectionPanel2.add(namePanel2);
		connectionPanel2.add(fieldPanel2);
    }
    public JScrollPane createTable() {
        sorter = new TableSorter();
        JTable table = new JTable(sorter);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sorter.addMouseListenerToHeaderInTable(table);
        JScrollPane scrollpane = new JScrollPane(table);
        return scrollpane;
    }
    public JScrollPane createTable2() {
        sorter2 = new TableSorter2();
        JTable table2 = new JTable(sorter2);
		//table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sorter2.addMouseListenerToHeaderInTable(table2);
        JScrollPane scrollpane2 = new JScrollPane(table2);
        return scrollpane2;
    }
    public void activateConnectionDialog() {
		/*if(JOptionPane.showOptionDialog(tableAggregate, connectionPanel, "con1",
		   JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
		                   null, ConnectOptionNames, ConnectOptionNames[0]) == 0) {
		    connect();
		}
		else
		    System.exit(0); */
		connect();
    }
    public void activateConnectionDialog2() {
		/*if(JOptionPane.showOptionDialog(tableAggregate2, connectionPanel2, "con2",
		   JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
		                   null, ConnectOptionNames2, ConnectOptionNames2[0]) == 0) {
		    connect2();
		}
		else
		    System.exit(0); */
		connect2();
    }

    public void connect() {
       dataBase = new JDBCAdapter(
            serverField.getText(),
            driverField.getText(),
            userNameField.getText(),
            passwordField.getText());
       sorter.setModel(dataBase);
   }
   public void connect2() {
       dataBase2 = new JDBCAdapter2(
            serverField2.getText(),
            driverField2.getText(),
            userNameField2.getText(),
            passwordField2.getText());
       sorter2.setModel(dataBase2);
   }

	public void fetch() {
		dataBase.executeQuery(queryTextArea.getText());
	}

	public void fetch2() {
		dataBase2.executeQuery(queryTextArea2.getText());
	}

	public void myEquityInsert() {
		try
		{
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + MYSCRIPS_DB, "root","deepak");
		    Statement statement = connection.createStatement();
		    String sql = "INSERT INTO " + cboMyEquity.getSelectedItem() + " (id, tdate, scrip, ttype, quantity, price, amount, taxbrk) VALUES (null,'"  +
		    textTransactionDate.getText() +  "','" +
		    textScripName.getText() +  "','" +
		    textTransactionType.getText() +  "'," +
		    textQuantity.getText() +  "," +
		    textPrice.getText() +  "," +
		    textAmountPaid.getText()  +  "," +
		    textTaxBrkPaid.getText()
		    + ")";
		    statement.executeUpdate(sql);
		    //System.out.println(sql);
		    connection.close();
		} catch(Exception e) {
		     System.out.println(e);
		}
	}
	public void  appendToTextAreaNseVolume(String item) {
	queryTextArea.setText(
		"SELECT nse.nse_nifty.tradedate AS DATE, SUBSTRING_INDEX(SUBSTRING_INDEX(nse.nse_nifty.ohlc,',',-1),',',1) AS NIFTY_VOL, "+
		"SUBSTRING_INDEX(SUBSTRING_INDEX(nse.nse_" + item + ".ohlc,',',-1),',',1)" + " AS " +
		item.toUpperCase() + "_VOL FROM nse.nse_nifty INNER JOIN "  +
		"nse.nse_" + item + " WHERE (nse.nse_nifty.tradedate=" +
		"nse.nse_" + item + ".tradedate) AND (nse.nse_"  + item + ".tradedate BETWEEN " +
		textFromDate1.getText() + " AND " + textToDate1.getText() +
		") ORDER BY nse.nse_nifty.tradedate");
	}
	public void  appendToTextAreaNsePercentage(String item) {
	queryTextArea2.setText(
		"SELECT nse.nse_nifty.tradedate AS DATE, SUBSTRING_INDEX(SUBSTRING_INDEX(nse.nse_nifty.ohlc,',',-2),',',1) AS NIFTY_CLOSE, "+
		"SUBSTRING_INDEX(SUBSTRING_INDEX(nse.nse_" + item + ".ohlc,',',-2),',',1)" + " AS " +
		item.toUpperCase() + "_CLOSE FROM nse.nse_nifty INNER JOIN "  +
		"nse.nse_" + item + " WHERE (nse.nse_nifty.tradedate=" +
		"nse.nse_" + item + ".tradedate) AND (nse.nse_"  + item + ".tradedate BETWEEN " +
		textFromDate2.getText() + " AND " + textToDate2.getText() +
		") ORDER BY nse.nse_nifty.tradedate");
	}
	public void clear_vectors() {
		niftyLows.removeAllElements();
		niftyHighs.removeAllElements();
	    niftyTradeDates.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
		niftyPrevCMPs.removeAllElements();
		niftyCMPs.removeAllElements();
		niftyScrips.removeAllElements();
		niftyPchange.removeAllElements();
		curNiftyIndex.removeAllElements();
		curNiftyVol.removeAllElements();
		curNiftyPC.removeAllElements();
		//for(int i= 0; i < 300; ++i) sumOfPchanges[i] = 0;
	}
}