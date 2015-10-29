/**
 * Version 1.0: Initial Code given 
 * Current: Version 1.1: Optimization and Refactor of code
 *                       Added New comments and cleaned loops
 *      Found Bugs: - Select > Symbology > Identity > Dimension > Condition > Function ID > Symbol Modifyer > Country > Order Battle
 *                    Order Battle is currently selectable as a menu heading, interferes when placing
 *                  - Tick boxes unclickable in left frame?
 */

//Necessary worldwind imports
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.util.measure.LengthMeasurer;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//Necessary swing imports
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;




import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.symbology.*;
import gov.nasa.worldwind.symbology.milstd2525.*;
import gov.nasa.worldwind.terrain.HighResolutionTerrain;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.BasicScheduledTaskService;
import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.LineBuilder;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.layers.Earth.MGRSGraticuleLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.airspaces.Airspace;
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes;
import gov.nasa.worldwind.render.airspaces.BasicAirspaceAttributes;
import gov.nasa.worldwind.render.airspaces.CappedCylinder;
import gov.nasa.worldwind.render.airspaces.Polygon;

//Necessary awt imports
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//All known bugs have been maked in comments as "BUG:"
public class Menus implements ActionListener, ItemListener {

    //Begin with variables
	private static SplashScreen splash;
    private String newline = "\n";
    public static String selectedMenuPath = "";
    public static String selectedMenuPath2 = "";
    private static WorldWindowGLJPanel wwd;
    public static int placingType = 0;
    private static ArrayList<Position> pathPositions = new ArrayList<Position>();
    private static boolean worldMapEnabled = true;
    private static boolean viewControlsEnabled = true;
    private static boolean scaleEnabled = true;
    private static boolean compassEnabled = true;
    private static boolean mgrsEnabled = false;
    private static boolean latLonEnabled = false;
    private static CreatedObject[] addedObjectArray = new CreatedObject[0];
    private static JList addedObjectsList = new JList();
    private static JPanel rightMasterPanel = new JPanel();
    private static JFrame masterFrame;
    private static JLabel latLabel = new JLabel();
    private static JLabel lonLabel = new JLabel();
    private static Map<String, String> map;
    protected HighResolutionTerrain terrain;
    private static RenderableLayer routelayer;
    private static AnnotationLayer annotationlayer;
    private static ArrayList<Line> lines = new ArrayList<Line>();
    private static AnnotationLayer globalAnnotationLayer;

    static JFrame frame;
    private JButton newButton;
    private JButton pauseButton;
    private JButton endButton;
    private JLabel[] pointLabels;
    private static LineBuilder lineBuilder;
    JPanel outerPanel = null;

    private static int AOcount = 0;
    private static int AOstatus = 0;
    
    private static int routecount = 0;
    
    //Timer used to update path menu, smaller number increases responsiveness and increases cpu requirements
    private final static int ONE_SECOND = 1000;

    //Create a tactical symbol to be added to the map using the passed in symbol code
    private static int addTacticalSymbols(double latitude, double longitude,
            double altitude, String symbolCode, String symbolName,
            String layerName) {
                //Setup symbol layer
        //BUG: Only one symbol can be on a layer with the same name so this breaks the layer toggle feature only toggling one of the symbols
        RenderableLayer layer = new RenderableLayer();
        layer.setName(layerName);

        // Create a the tactical symbol using the symbolCode
        TacticalSymbol symbol = new MilStd2525TacticalSymbol(symbolCode, Position.fromDegrees(latitude, longitude, altitude));
        // Tool tip text
        symbol.setValue(AVKey.DISPLAY_NAME, symbolName);
        //Turn off the location attached to a symbol
        symbol.setShowLocation(false);
		//Symbol atributes to change size and opacity
        //Scale - material - opacity - material - material
        symbol.setAttributes(new BasicTacticalSymbolAttributes(0.5, null, 1.0, null, null));
        layer.addRenderable(symbol);

        // Add the symbol layer to the World Wind model.
        wwd.getModel().getLayers().add(layer);
        wwd.updateUI();
        return wwd.getModel().getLayers().indexOf(layer, 0);
    }


    /** Method for placing Friendly / Threat Domes (GeoZones) - Invoked from createGenericSymbologyDetails
     * 
     * @param lat
     * @param lon
     * @param radius
     * @return 
     */
    private static int placeGeoZone(double lat, double lon, double radius) {
        //Place an ellipsoid at the input location with the input radius
        ShapeAttributes attrs = new BasicShapeAttributes();
        //Can eventually input the colour into this method header to change the colour depending on hostility
        attrs.setInteriorMaterial(Material.RED);
        attrs.setInteriorOpacity(0.7);
        attrs.setEnableLighting(true);
        attrs.setOutlineMaterial(Material.RED);
        attrs.setOutlineWidth(2d);
        attrs.setDrawInterior(true);
        attrs.setDrawOutline(false);

        //Create the layer for the geozone
        //BUG: Only one Geo Zone can be on a layer with the same name so this breaks the layer toggle feature only toggling one of the Geo Zones
        RenderableLayer layer = new RenderableLayer();
        layer.setName("GeoZone Layer");
        Ellipsoid sphere = new Ellipsoid(Position.fromDegrees(lat, lon), radius, radius, radius);
        sphere.setAttributes(attrs);
        sphere.setAltitudeMode(WorldWind.ABSOLUTE);
        sphere.setVisible(true);
        layer.addRenderable(sphere);


        
        //Add the layer to the World wind model
        wwd.getModel().getLayers().add(layer);
        wwd.updateUI();
        return wwd.getModel().getLayers().indexOf(layer, 0);
    }




	/** Setup a basic square AO using two corner positions
     * 
     * @param positions
     */
    private static int addBasicAO(List<Position> positions) {
        //Setup AO layer
        //BUG: Only one AO can be on a layer with the same name so this breaks the layer toggle feature only toggling one of the AO
        RenderableLayer layer = new RenderableLayer();
        layer.setName("AO Layer");
        Path path = new Path(positions);

        // Create and set an attribute bundle.
        ShapeAttributes attrs = new BasicShapeAttributes();
        attrs.setOutlineMaterial(new Material(WWUtil.makeRandomColor(null)));
        attrs.setOutlineWidth(2d);
        path.setAttributes(attrs);
        path.setVisible(true);
        path.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        path.setPathType(AVKey.GREAT_CIRCLE);
        path.setExtrude(true);
        path.setPathType(AVKey.RHUMB_LINE);
        
        
        //AO colour can be changed using the below attributes, currently set to random
        attrs = new BasicShapeAttributes();
        attrs.setOutlineMaterial(new Material(WWUtil.makeRandomColor(null)));
        attrs.setInteriorMaterial(new Material(WWUtil.makeRandomColor(null)));
        attrs.setOutlineWidth(2);
        path.setAttributes(attrs);
        layer.addRenderable(path);

        //Add AO to World Wind model
        wwd.getModel().getLayers().add(layer);
        wwd.updateUI();
        return wwd.getModel().getLayers().indexOf(layer, 0);
    }

    /** Method for creating a route/path
     * 
     * @param positions
     * @return 
     */
      private static int addBasicPath(List<Position> positions) {
        //BUG: Only one path can be on a layer with the same name so this breaks the layer toggle feature only toggling one of the path
        RenderableLayer layer = new RenderableLayer();
        layer.setName("Route Layer");
        //Path path = new Path(positions);
     
        
        
        /** OLD METHOD - Didn't allow for measuring
        //Create and set an attribute bundle.
        //Colour and width can be changed her
        //ShapeAttributes attrs = new BasicShapeAttributes();
        //attrs.setOutlineMaterial(new Material(Color.red));
        //attrs.setOutlineWidth(100);
        //attrs.setDrawOutline(true);
        //path.setAttributes(attrs);
        //path.setVisible(true);
        //path.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        //path.setPathType(AVKey.RHUMB_LINE);
        */
        
        
		//this flag will be false for air travel
		//measure.setFollowTerrain(true);
		
		//path.setOffset(arg0);
        
        

        
        //layer.addRenderable(path);

        //addObjectToAddedList("New Route", path);
        
        //Add path to world wind model
        wwd.getModel().getLayers().add(layer);
        wwd.updateUI();
        return wwd.getModel().getLayers().indexOf(layer, 0);
    }




	//Setting up the menu
    //Note: This is the method that makes the application take a little while to launch
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menuSelect = null,
                menuLevel1 = null,
                menuLevel2 = null,
                menuLevel3 = null,
                menuLevel4 = null,
                menuLevel5 = null,
                menuLevel6 = null,
                menuLevel7 = null,
        		menuLevel8 = null;
        // Create the menu bar.
        menuBar = new JMenuBar();

        // Build the first menu.
        menuSelect = new JMenu("Select");
        menuBar.add(menuSelect);

        // Open textfiles for the menu text
        String[] mainMenu = openMenusTxt("MainMenu.txt");
        String[] standardIdentity = openMenusTxt("StandardIdentity.txt");
        String[] battleDimension = openMenusTxt("BattleDimension.txt");
        String[] statusCondition = openMenusTxt("StatusCondition.txt");
        String[] functionID = openMenusTxt("FunctionID.txt");
        String[] symbolModifier = openMenusTxt("SymbolModifier.txt");
        String[] country = openMenusTxt("Country.txt");
        String[] orderOfBattle = openMenusTxt("OrderOfBattle.txt");
        //String[] airspaceTypes = openMenusTxt("AirspaceTypes.txt");
        
        

        
        // Building the menus and submenus from text files
        for (int i = 0; i < mainMenu.length; i++) {
            if (mainMenu[i].substring(0, 1).toString().equals("*")) {
                menuLevel1 = addSubMenuItem(menuSelect, menuLevel1,
                        mainMenu[i].substring(1, mainMenu[i].toString().length()), this, standardIdentity);

                for (int j = 0; j < standardIdentity.length; j++) {
                    if (standardIdentity[j].substring(0, 1).toString().equals("*")) {
                        menuLevel2 = addSubMenuItem(menuLevel1, menuLevel2,
                                standardIdentity[j].substring(1, standardIdentity[j].toString().length()), this, battleDimension);

                        for (int k = 0; k < battleDimension.length; k++) {
                            if (battleDimension[k].substring(0, 1).toString().equals("*")) {
                                menuLevel3 = addSubMenuItem(menuLevel2, menuLevel3, battleDimension[k].substring(1, battleDimension[k].toString().length()), this, statusCondition);

                                for (int l = 0; l < statusCondition.length; l++) {
                                    if (statusCondition[l].substring(0, 1).toString().equals("*")) {
                                        menuLevel4 = addSubMenuItem(menuLevel3, menuLevel4, statusCondition[l].substring(1, statusCondition[l].toString().length()), this, functionID);

                                        for (int m = 0; m < functionID.length; m++) {
                                            if (functionID[m].substring(0, 1).toString().equals("*")) {
                                                menuLevel5 = addSubMenuItem(menuLevel4, menuLevel5, functionID[m].substring(1, functionID[m].toString().length()), this, symbolModifier);

                                                for (int n = 0; n < symbolModifier.length; n++) {
                                                    if (symbolModifier[n].substring(0, 1).toString().equals("*")) {
                                                        menuLevel6 = addSubMenuItem(menuLevel5, menuLevel6, symbolModifier[n].substring(1, symbolModifier[n].toString().length()), this, country);

                                                        for (int o = 0; o < country.length; o++) {
                                                            if (country[o].substring(0, 1).toString().equals("*")) {
                                                                menuLevel7 = addSubMenuItem(menuLevel6, menuLevel7, country[o].substring(1, country[o].toString().length()), this, orderOfBattle);
                                                            } 
                                                            else {
                                                                addNonSubMenuItem(menuLevel6, country[o].toString());
                                                            }
                                                        }
                                                    } 
                                                    else {
                                                        addNonSubMenuItem(menuLevel5, symbolModifier[n].toString());
                                                    }
                                                }
                                            } 
                                            else {
                                                addNonSubMenuItem(menuLevel4, functionID[m].toString());
                                            }
                                        }
                                    } 
                                    else {addNonSubMenuItem(menuLevel3, statusCondition[l].toString());
                                    }
                                }
                            } 
                            else {addNonSubMenuItem(menuLevel2,battleDimension[k].toString());
                            }
                        }
                    } 
                    else {addNonSubMenuItem(menuLevel1, standardIdentity[j].toString());
                    }
                }
            }
            else {
                JMenuItem item = new JMenuItem(mainMenu[i].toString());
                item.addActionListener(this);
                menuSelect.add(item);
            }
        }

        //Add Airspace Menu Selectors
        JMenu airSpace = new JMenu("Air Space");
        //menuitems
        JMenuItem airspaceCylinder = new JMenuItem("Cylinder");
        JMenuItem airspaceCube = new JMenuItem("Cube");
        airspaceCylinder.addActionListener(this);
        airspaceCube.addActionListener(this);
        
        airSpace.add(airspaceCube);
        airSpace.add(airspaceCylinder);
        
        menuSelect.add(airSpace);
        
        
        //Add separator
        ((JMenu) menuSelect).addSeparator();
        //Add Exit option
        JMenuItem menuExit = new JMenuItem("Exit", null);
        menuExit.addActionListener(this);
        menuSelect.add(menuExit);
        menuBar.add(menuSelect);

        //Add timer (dont select item to quick;y)
        Timer timer = new Timer(ONE_SECOND, new ActionListener() {
            //Setting up the string to be used for symbol codes creation
            public void actionPerformed(ActionEvent evt) {
                StringBuilder strPath = new StringBuilder();
                MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();
                for (int i = 0; i < path.length; i++) {
                    if (path[i].getComponent() instanceof javax.swing.JMenuItem) {
                        JMenuItem mi = (JMenuItem) path[i].getComponent();
                        if ("".equals(mi.getText())) {
                            strPath.append("ICON-ONLY MENU ITEM > ");
                        } else {
                            strPath.append(mi.getText() + ",");
                        }
                    }
                }
                //Bug: If selecting from the last menu when creating a symbol too quickly it can miss out on getting the last symbol code. 
                //This will result in illegal symbol code
                //Can be fixed by changing the timer above to a shorter number
                selectedMenuPath2 = strPath.toString();
            }
        });
        timer.start();
        return menuBar;
    }

     // Create the content-pane-to-be.
    public Container createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        return contentPane;
    }

    //Deciding what menu item has been selected and acting accordingly by setting placement type 
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String s = "Action event detected." + newline + "    Event source: " + source.getText() + " (an instance of " + getClassName(source) + ")";
        
        // AO
        if (e.getActionCommand().toString().endsWith("AO")) {
            System.out.println("AO SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            //Note: this can be used for a more advanced form of ao or path creation
            //makePanel("AO", new Dimension(200,400));
            placingType = 2;
            createAnnotation("AO Selected - Presse Enter to Finish Placing Area");
        }
        // Geo Zone
        if (e.getActionCommand().toString().endsWith("Geo Zone")) {
            System.out.println("Geo Zone Selected");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 3;
        }
        // Route
        if (e.getActionCommand().toString().endsWith("Route")) {
            System.out.println("Route SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            //Note: this can be used for a more advanced form of ao or path creation
            //makePanel("AO", new Dimension(200,400));
            createAnnotation("Route Selected - Press Enter to Finish Placing Route");
            placingType = 4;
        }
        // Role 1
        if (e.getActionCommand().toString().endsWith("Role 1")) {
            System.out.println("Role 1 SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 6;
            selectedMenuPath = "SFGPUSM----EASG";
            createAnnotation("Role 1 Medical Facility Selected - Click to Place");
            System.out.println(selectedMenuPath + "****");
        }
        // Role 2
        if (e.getActionCommand().toString().endsWith("Role 2")) {
            System.out.println("Role 2 SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 6;
            selectedMenuPath = "SFGPUSM----FASG";
            createAnnotation("Role 2 Medical Facility Selected - Click to Place");
        }
        // Role 3
        if (e.getActionCommand().toString().endsWith("Role 3")) {
            System.out.println("Role 3 SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 6;
            selectedMenuPath = "SFGPUSM----HASG";
            createAnnotation("Role 3 Medical Facility Selected - Click to Place");
        }
        // Enter Code
        if (e.getActionCommand().toString().endsWith("Enter Code")) {
            System.out.println("Enter Code SELECTED");
            System.out.println("Path = " + selectedMenuPath);
        }
        // Ground OB
        if (e.getActionCommand().toString().endsWith("Ground OB")) {
            System.out.println("Ground OB SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 1;
            selectedMenuPath = getSymbolLink();
            createAnnotation("Ground OB Selected - Click to Place");
            System.out.println("New menu path = " + selectedMenuPath);
        }
        // Civilian OB
        if (e.getActionCommand().toString().endsWith("Civilian OB")) {
            System.out.println("Civilian OB SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 1;
            createAnnotation("Civilian OB Selected - Click to Place");
            selectedMenuPath = getSymbolLink();
        }
        // Maritime OB
        if (e.getActionCommand().toString().endsWith("Maritime OB")) {
            System.out.println("Maritime OB SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 1;
            createAnnotation("Maritime OB Selected - Click to Place");
            selectedMenuPath = getSymbolLink();
        }
        // Casualty
        if (e.getActionCommand().toString().endsWith("Casualty")) {
            System.out.println("Casualty SELECTED");
            System.out.println("Path = " + selectedMenuPath);
            placingType = 5;
            createAnnotation("Casualty Selected - Click to Place");
            selectedMenuPath = getSymbolLink();
        }
        
        // Cube Airspace
        if (e.getActionCommand().toString().endsWith("Cube")) {
        	System.out.println("CUBE AIRSPACE SELECTED");
        	placingType = PlacementType.AIRSPACE_BOX;
        	createAnnotation("Cube Airspace Selected - Click to Place");
        }
        
        // Cylinder Airspace
        if (e.getActionCommand().toString().endsWith("Cylinder")) {
        	System.out.println("CYLINDER AIRSPACE SELECTED");
        	placingType = PlacementType.AIRSPACE_CYLINDER;
        	createAnnotation("Cylinder Airspace Selected - Click to Place");
        }
        
        // Exit
        if (e.getActionCommand().toString().endsWith("Exit")) {
        	System.exit(0);
        }
        
    }

    /** Method for placing annotations
     * 
     * @param string
     */
    private void createAnnotation(String string) {

		//clear annotation layer first.
    	this.annotationlayer.removeAllAnnotations();
    	
    	//point for half width (center) of map
    	//Point point = new Point(this.wwd.getWidth()/2, this.wwd.getHeight()-120);
    	
    	//draw new annotation at top center of screen dynamically.
    	//ScreenAnnotation newAnnotation = new ScreenAnnotation(string, point);
    	ScreenRelativeAnnotation newAnnotation = new ScreenRelativeAnnotation(string,.5,.9);
    	newAnnotation.getAttributes().setBackgroundColor(Color.black);
    	newAnnotation.getAttributes().setTextColor(Color.WHITE);
    	newAnnotation.getAttributes().setTextAlign(AVKey.CENTER);
    	newAnnotation.getAttributes().setBorderColor(Color.white);
    	newAnnotation.getAttributes().setFont(Font.decode("Arial-BOLD-14"));
    	newAnnotation.setAlwaysOnTop(true);
    	newAnnotation.getAttributes().setSize(new Dimension(400,0));
    	
    	//adds annotation to annotation layer
    	annotationlayer.addAnnotation(newAnnotation);
	}


	public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String s = "Item event detected."
                + newline
                + "    Event source: "
                + source.getText()
                + " (an instance of "
                + getClassName(source)
                + ")"
                + newline
                + "    New state: "
                + ((e.getStateChange() == ItemEvent.SELECTED) ? "selected" : "unselected");
    }

    // Returns just the class name -- no package info.
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex + 1);
    }

    //Returns an ImageIcon, or null if the path was invalid.
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Menus.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //Sets up the left side of the screen
    /** Method for creating the left-hand-side panel for the object list and checkboxes
     * 
     * @return
     */
    private static JSplitPane createLeftPanel() {
        //Note this can be modified to include colapseable buttons
        JSplitPane panel = new JSplitPane();
        panel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        panel.setOneTouchExpandable(true);
        panel.setDividerLocation(150);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 0));

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(200, 200));
        bottomPanel.setPreferredSize(new Dimension(200, 300));
        bottomPanel.setLayout(new FlowLayout());

        //Add top panel content
        addedObjectsList.setPreferredSize(new Dimension(190, 500));
        addedObjectsList.setListData(addedObjectArray);

        
        //creates delete right-click context menu
		final JPopupMenu menu = new JPopupMenu();
		JMenuItem delete = new JMenuItem("Delete");
		menu.add(delete);

		addedObjectsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && !addedObjectsList.isSelectionEmpty() && addedObjectsList.locationToIndex(me.getPoint()) == addedObjectsList.getSelectedIndex()) {
					menu.show(addedObjectsList, me.getX(), me.getY());
				}
			}
		});

		delete.addActionListener(new ActionListener() {

			//Removes item from addedobjectsarray - jlist (addedobjectslist) is updated also
			//TODO: remove item that's drawn on map also.
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<CreatedObject> copyList = new ArrayList<CreatedObject>(Arrays.asList(addedObjectArray));
				
				//undraw item
				String sw = addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType();
				if(sw.contentEquals("AO")){
					AO tempAo = addedObjectArray[addedObjectsList.getSelectedIndex()].getAo();
					wwd.getModel().getLayers().remove(tempAo.getPos());
				}
				else if(sw.contentEquals("GeoZone")){
					GeoZone tempGZ = addedObjectArray[addedObjectsList.getSelectedIndex()].getGeoZone();
					System.out.println("removing geoZone" + tempGZ.getPos()+ "   " + wwd.getModel().getLayers().size());
					wwd.getModel().getLayers().remove(tempGZ.getPos());
				}
				else if(sw.contentEquals("Route")){
					Route tempRoute = addedObjectArray[addedObjectsList.getSelectedIndex()].getRoute();
					System.out.println("removing route" + tempRoute.getPos()+ "   " + wwd.getModel().getLayers().size());
					wwd.getModel().getLayers().remove(tempRoute.getPos());
					
				}
				else if(sw.contains("MedicalSymbology")){
					MecialSymbology tempMS = addedObjectArray[addedObjectsList.getSelectedIndex()].getMedicalSymbology();
					System.out.println("removing MedicalSymbology" + tempMS.getPos()+ "   " + wwd.getModel().getLayers().size());
					wwd.getModel().getLayers().remove(tempMS.getPos());
				}
				else if(sw.contains("GenericSymbology")){
					GenericSymbology tempGS = addedObjectArray[addedObjectsList.getSelectedIndex()].getGenericSymbology();
					System.out.println("removing GenericSymbology" + tempGS.getPos()+ "   " + wwd.getModel().getLayers().size());
					wwd.getModel().getLayers().remove(tempGS.getPos());
				}
				else if(sw.contains("POI")){
					POI tempPOI = addedObjectArray[addedObjectsList.getSelectedIndex()].getPoi();
					System.out.println("removing POI" + tempPOI.getPos()+ "   " + wwd.getModel().getLayers().size());
					wwd.getModel().getLayers().remove(tempPOI.getPos());
				}
				
				copyList.remove(addedObjectsList.getSelectedValue());
				copyList.toArray(addedObjectArray);
				
				System.out.println("Removed Object from Object List");
									
				
				addedObjectsList.setSelectedIndex(-1);
				addedObjectsList.setListData(addedObjectArray);
				
				addedObjectsList.revalidate();
				addedObjectsList.repaint();
				wwd.updateUI();
				
			}
			
		});
		
		
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setViewportView(addedObjectsList);
        scrollPane2.setPreferredSize(new Dimension(200, 550));
        addedObjectsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                masterFrame.remove(rightMasterPanel);
                rightMasterPanel = new JPanel();
                rightMasterPanel.setPreferredSize(new Dimension(300, 800));
                masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
                masterFrame.repaint();
                masterFrame.revalidate();
                System.out.println(addedObjectsList.getSelectedIndex());
                try {
                    setupDetailsView(addedObjectArray[addedObjectsList.getSelectedIndex()]);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        });
        topPanel.add(scrollPane2);

        //Add bottom panel content			
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(190, 280));
        
        
        //Creates the checkboxes for layer management
        //Could potentially be swapped for world winds layer management if layers are setup correctly
        JCheckBox pathsCheckBox = new JCheckBox();
        pathsCheckBox.setPreferredSize(new Dimension(200, 25));
        pathsCheckBox.setText("Paths");
        pathsCheckBox.setSelected(true);
        pathsCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        pathsCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("Route Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("Route Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(pathsCheckBox);

        JCheckBox aoCheckBox = new JCheckBox();
        aoCheckBox.setPreferredSize(new Dimension(200, 25));
        aoCheckBox.setText("AO");
        aoCheckBox.setSelected(true);
        aoCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        aoCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("AO Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("AO Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(aoCheckBox);

        JCheckBox friendlySymbolCheckBox = new JCheckBox();
        friendlySymbolCheckBox.setPreferredSize(new Dimension(200, 25));
        friendlySymbolCheckBox.setText("Friendly Symbol");
        friendlySymbolCheckBox.setSelected(true);
        friendlySymbolCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        friendlySymbolCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("My Symbol Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("My Symbol Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(friendlySymbolCheckBox);

        JCheckBox friendlyGeoZoneCheckBox = new JCheckBox();
        friendlyGeoZoneCheckBox.setPreferredSize(new Dimension(200, 25));
        friendlyGeoZoneCheckBox.setText("Friendly GeoZones");
        friendlyGeoZoneCheckBox.setSelected(true);
        friendlyGeoZoneCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        friendlyGeoZoneCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("GeoZone Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("GeoZone Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(friendlyGeoZoneCheckBox);

        JCheckBox enemySymbolsCheckBox = new JCheckBox();
        enemySymbolsCheckBox.setPreferredSize(new Dimension(200, 25));
        enemySymbolsCheckBox.setText("Enemy Symbol");
        enemySymbolsCheckBox.setSelected(true);
        enemySymbolsCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        enemySymbolsCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("My Symbol Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("My Symbol Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(enemySymbolsCheckBox);

        JCheckBox enemyGeoZonesCheckBox = new JCheckBox();
        enemyGeoZonesCheckBox.setPreferredSize(new Dimension(200, 25));
        enemyGeoZonesCheckBox.setText("Enemy GeoZones");
        enemyGeoZonesCheckBox.setSelected(true);
        enemyGeoZonesCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        enemyGeoZonesCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox tempCheckBox = (JCheckBox) e.getSource();
                if (tempCheckBox.isSelected()) {
                    wwd.getModel().getLayers().getLayerByName("GeoZone Layer").setEnabled(true);
                } else {
                    wwd.getModel().getLayers().getLayerByName("GeoZone Layer").setEnabled(false);
                }
            }
        });
        bottomPanel.add(enemyGeoZonesCheckBox);
        topPanel.setMinimumSize(new Dimension(100, 100));
        bottomPanel.setMinimumSize(new Dimension(100, 100));
        //Adds content to the left panels
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }






    //The next few methods setup java swing components for a selected object to be placed on the right panel


private static void createAirspaceDetails(final AirSpace as) {
    	
    	final JPanel panel = new JPanel();
    	panel.setPreferredSize(new Dimension(300,800));
    	JLabel asNameLabel = new JLabel("Airspace ID");
    	asNameLabel.setPreferredSize(new Dimension(150,25));
    	final JTextField asIDTextField = new JTextField();
    	asIDTextField.setPreferredSize(new Dimension(140,25));
    	asIDTextField.setText(as.getName());
    	
    	//add generic labels/fields
    	panel.add(asNameLabel);
    	panel.add(asIDTextField);
    	
    	switch (as.getType()) {
    	case PlacementType.AIRSPACE_BOX:
    		
    		final LatLon[] p = new LatLon[as.getPos().size()];
    		int i = 0;
    		for (LatLon ll : as.getPos()) {
    			p[i] = as.getPos().get(i);
    			i++;
    		}
    		JLabel latLabel = new JLabel("Latitude");
    		latLabel.setPreferredSize(new Dimension(150, 25));
    		final JTextField boxlatTextField = new JTextField();
    		boxlatTextField.setPreferredSize(new Dimension(140, 25));
    		JLabel lonLabel = new JLabel("Longitude");
    		lonLabel.setPreferredSize(new Dimension(150, 25));
    		final JTextField boxlonTextField = new JTextField();
    		boxlonTextField.setPreferredSize(new Dimension(140, 25));
    		

    		
    		JLabel asLatLonListLabel = new JLabel("Points(Lat,Lon)");
    		asLatLonListLabel.setPreferredSize(new Dimension(100, 25));
    		final JList<LatLon> asLatLonPoints = new JList<LatLon>(p);
    		asLatLonPoints.setPreferredSize(new Dimension(280, 150));
    		asLatLonPoints.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					try {
						boxlatTextField.setText(p[asLatLonPoints.getSelectedIndex()].getLatitude().toString());
						boxlonTextField.setText(p[asLatLonPoints.getSelectedIndex()].getLongitude().toString());
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
					}
					
				}
    			
    		});
    		
    		//Upper Bounds
    		JLabel boxasUpperLabel = new JLabel("Upper Altitude");
    		boxasUpperLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField boxupperTextField = new JTextField();
    		boxupperTextField.setPreferredSize(new Dimension(140,25));
    		boxupperTextField.setText(Double.toString(as.getUpper()));
    		    		
    		//Lower Bounds
    		JLabel boxasLowerLabel = new JLabel("Lower Altitude");
    		boxasLowerLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField boxlowerTextField = new JTextField();
    		boxlowerTextField.setPreferredSize(new Dimension(140,25));
    		boxlowerTextField.setText(Double.toString(as.getLower()));
    		
    		KeyAdapter AODetailsKeyAdapter = new KeyAdapter(){
    			
    		};
    		
    		//Longitude editor
    		boxlonTextField.addKeyListener(new KeyAdapter() {
    			public void keyPressed(KeyEvent e) {
    				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    					
    					Angle longitude = null;
    					Angle latitude = null;
    					
    					latitude = latitude.fromDegrees(Double.parseDouble(boxlatTextField.getText().replace("°", "")));
    					longitude = longitude.fromDegrees(Double.parseDouble(boxlonTextField.getText().replace("°", "")));
    					
    					LatLon tempLatLon = new LatLon(latitude,longitude);
    					
    					int i = 0;
    					for (LatLon ll : p) {
    						if (p[i] == asLatLonPoints.getSelectedValue()) {
    							p[i] = tempLatLon;
    						}
    						
    						i++;
    					}
    					
    					ArrayList<Position> newPos = new ArrayList<Position>();
    					for (LatLon np : p) {
    						newPos.add(new Position(np.getLatitude(), np.getLongitude(),0));
    					}
    					
    					AirSpace tempAS = new AirSpace(PlacementType.AIRSPACE_BOX, newPos, Double.parseDouble(boxupperTextField.getText()), Double.parseDouble(boxlowerTextField.getText()));
    					CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), tempAS);
    					replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);
    					wwd.getModel().getLayers().remove(tempAS.getName());
    					
    					addAirspace(PlacementType.AIRSPACE_BOX, tempAS.getPos());    					
    					
    					panel.requestFocus();
    					
    					//asLatLonPoints.setListData(p);
    					
    					asLatLonPoints.validate();
    					asLatLonPoints.repaint();
    				}
    			}
    		});
    		
    		
    		//apply button
    		JButton boxapplyButton = new JButton();
    		boxapplyButton.setText("Apply");
    		boxapplyButton.setPreferredSize(new Dimension(300, 25));
    		boxapplyButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Finish ApplyButton Action Listener for Box Airspace editing
					
				}
    			
    		});
    		
    		
    		
    		panel.add(asLatLonListLabel);
    		panel.add(asLatLonPoints);
    		panel.add(latLabel);
    		panel.add(boxlatTextField);
    		panel.add(lonLabel);
    		panel.add(boxlonTextField);
    		panel.add(boxasUpperLabel);
    		panel.add(boxupperTextField);
    		panel.add(boxasLowerLabel);
    		panel.add(boxlowerTextField);
    		panel.add(boxapplyButton);
    		
    		
    		
    		break;
    	case PlacementType.AIRSPACE_CYLINDER:
    		
    		//Center position
    		JLabel asLatLabel = new JLabel("Latitude");
    		asLatLabel.setPreferredSize(new Dimension(150,25));
    		JLabel asLonLabel = new JLabel("Longitude");
    		asLonLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField latTextField = new JTextField();
    		latTextField.setPreferredSize(new Dimension(140, 25));
    		latTextField.setText(as.getPos().get(0).getLatitude().toString());
    		final JTextField lonTextField = new JTextField();
    		lonTextField.setPreferredSize(new Dimension(140, 25));
    		lonTextField.setText(as.getPos().get(0).getLongitude().toString());
    		
    		//Radius
    		JLabel asRadiusLabel = new JLabel("Radius");
    		asRadiusLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField radiusTextField = new JTextField();
    		radiusTextField.setPreferredSize(new Dimension(140, 25));
    		radiusTextField.setText(Double.toString(as.getRadius()));
    		
    		//Upper Bounds
    		JLabel asUpperLabel = new JLabel("Upper Altitude");
    		asUpperLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField upperTextField = new JTextField();
    		upperTextField.setPreferredSize(new Dimension(140,25));
    		upperTextField.setText(Double.toString(as.getUpper()));
    		    		
    		//Lower Bounds
    		JLabel asLowerLabel = new JLabel("Lower Altitude");
    		asLowerLabel.setPreferredSize(new Dimension(150,25));
    		final JTextField lowerTextField = new JTextField();
    		lowerTextField.setPreferredSize(new Dimension(140,25));
    		lowerTextField.setText(Double.toString(as.getLower()));
    		
    		
    		//apply button
    		JButton cylapplyButton = new JButton();
    		cylapplyButton.setText("Apply");
    		cylapplyButton.setPreferredSize(new Dimension(300, 25));
    		cylapplyButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Finish ApplyButton Action Listener for Cylinder Airspace editing
					
				}
    			
    		});
    		
    		//add all labels/textfields
    		
    		panel.add(asLatLabel);
    		panel.add(latTextField);
    		panel.add(asLonLabel);
    		panel.add(lonTextField);
    		panel.add(asRadiusLabel);
    		panel.add(radiusTextField);
    		panel.add(asUpperLabel);
    		panel.add(upperTextField);
    		panel.add(asLowerLabel);
    		panel.add(lowerTextField);
    		panel.add(cylapplyButton);
    		
    		break;
    	}
    	
    	masterFrame.remove(rightMasterPanel);
    	rightMasterPanel = new JPanel();
    	rightMasterPanel.setPreferredSize(new Dimension(300,800));
    	rightMasterPanel.add(panel);
    	masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
    	masterFrame.repaint();
    	masterFrame.validate();

    }


	private static void checkThreatAreas() {
    	
		//lines.clear();
    	
    	/* Generate array of lines for each route in addedObjectArray
    	 * 
    	 * for each GeoZone in addedObjectArray
    	 * 		if (line.distanceTo(GeoZone center) < geoZone.radius)
    	 * 			therefore intersecting
    	 * 
    	 */
    	
    	
    	HighResolutionTerrain terrain = new HighResolutionTerrain(wwd.getModel().getGlobe(), 20d);
    	System.out.println("Checking for Route-GeoZone Collisions");
    	for (CreatedObject co : addedObjectArray) {
    	   	//Routes
    		if (co.getObjectType() == "Route") {
    			Route tempRoute = co.getRoute();
    			//generates list of lines to check
    			for (int i = 0 ; i < tempRoute.getPathCoords().size() - 1; i++) {
    				Vec4 refPoint = terrain.getSurfacePoint(tempRoute.getPathCoords().get(i));
    				Vec4 tarPoint = terrain.getSurfacePoint(tempRoute.getPathCoords().get(i+1));
    				Line line = new Line(tarPoint, refPoint.subtract3(tarPoint));
    				lines.add(line);
    			}
    			
    		}
    		
    		//GeoZone
    		if (co.getObjectType() == "GenericSymbology") {
    			GeoZone tempGZ = co.getGenericSymbology().getGeoZone();
    			//System.out.println("Geo Zone Radius " + tempGZ.getradius());
    			for (Line l : lines) {
    				//System.out.println(l.toString());
    				if (l.distanceTo(terrain.getSurfacePoint(tempGZ.getArea())) < tempGZ.getradius()) {
    					Position pos = new Position(wwd.getModel().getGlobe().computePositionFromPoint(l.getOrigin()), 0);
    					//System.out.println(pos.toString());
    					createPointAnnotation(pos, "DANGER! Route Passing through Threat Area");
    					//System.out.println("INTERSECTION");
    				}
    			}
    			
    			
    			
    		}
    		
   		
    		
    	}
    }	

	
    private static void createPointAnnotation(Position point, String annotation) {
    	
    	globalAnnotationLayer.removeAllAnnotations();
    	GlobeAnnotation ga = new GlobeAnnotation(annotation, point);
    	AnnotationAttributes aa = new AnnotationAttributes();
    	aa.setBackgroundColor(Color.red);
    	aa.setTextColor(Color.white);
    	aa.setFont(Font.decode("Arial-BOLD-14"));
    	ga.setAttributes(aa);
    	ga.setPickEnabled(false);
        globalAnnotationLayer.addAnnotation(ga);
    	
    	//wwd.getModel().getLayers().add(globalAnnotationLayer);
    	//annotationlayer.addAnnotation(ga);
    	wwd.redraw();
    	
    }


    //TODO: Detail views apply buttons update the data stored about each of the objects however they do not update any features on the map
    private static void createAODetails(final AO ao) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));

        JLabel aoNameLabel = new JLabel("AO ID");
        aoNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField aoIDTextField = new JTextField();
        aoIDTextField.setPreferredSize(new Dimension(150, 25));
        aoIDTextField.setText(ao.getAoID());

        JLabel opNameLabel = new JLabel("OP Name");
        opNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField opNameTextField = new JTextField();
        opNameTextField.setPreferredSize(new Dimension(150, 25));
        opNameTextField.setText(ao.getOpName());

        JLabel classificationLabel = new JLabel("Classification");
        classificationLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField classificationTextField = new JTextField();
        classificationTextField.setPreferredSize(new Dimension(150, 25));
        classificationTextField.setText(ao.getClassification());

        JLabel commentsLabel = new JLabel("Points(Lat,Lon)");
        final JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setPreferredSize(new Dimension(280, 150));
        for(int i = 0; i<ao.getAoCoords().size()-1;i++){
        	Point2D item = ao.getAoCoords().get(i);
        	if(i==0){
        		commentsTextArea.setText(commentsTextArea.getText()+"Point"+i+" "+item.getX()+" "+item.getY());
        	}
        	else
        		commentsTextArea.setText(commentsTextArea.getText()+"\nPoint"+i+" "+item.getX()+" "+item.getY());
        }

        /*
        JLabel aoPoint1Label = new JLabel("NW Corner Point");
        aoPoint1Label.setPreferredSize(new Dimension(100, 25));
        final JTextField latTextField = new JTextField();
        latTextField.setPreferredSize(new Dimension(50, 25));
        final JTextField lonTextField = new JTextField();
        lonTextField.setPreferredSize(new Dimension(50, 25));

        JLabel aoPoint2Label = new JLabel("SE Corner Point");
        aoPoint2Label.setPreferredSize(new Dimension(100, 25));
        final JTextField lat2TextField = new JTextField();
        lat2TextField.setPreferredSize(new Dimension(50, 25));
        final JTextField lon2TextField = new JTextField();
        lon2TextField.setPreferredSize(new Dimension(50, 25));
*/
        
        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.setPreferredSize(new Dimension(300, 25));
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Apply Pressed");
                AO tempAo = addedObjectArray[addedObjectsList.getSelectedIndex()].getAo();
                tempAo.setAoID(aoIDTextField.getText());
                tempAo.setOpName(opNameTextField.getText());
                tempAo.setClassification(classificationTextField.getText());
                
                String temp = commentsTextArea.getText();
                System.out.println(temp);
                
                
                tempAo.setComments(commentsTextArea.getText());
                //List<String> newPoints= new ArrayList<String>();
                String lines[] = temp.split("\\r?\\n");
                ArrayList<Point2D> tempAOpath = new ArrayList<Point2D>();
                ArrayList<Position> tempP = new ArrayList<Position>();
                
                int count=0;
                
                Point2D tail = null;
                Position tailP = null;
                Angle Lat = null;
                Angle Lon = null;
                
                
                for(String item : lines){
            
                	Point2D temp2D;
                	Position tempPP;
                	
                	String coors[] = item.split(" ");
                	System.out.println(coors.length+"hey i am here");
                	if(count == 0){
                		tail = new Point2D.Double(Double.parseDouble(coors[1]),Double.parseDouble(coors[2]));
                		tailP = new Position(Lat.fromDegrees(Double.parseDouble(coors[1])),Lon.fromDegrees(Double.parseDouble(coors[2])),1e4);
                	}
                	tempPP = new Position(Lat.fromDegrees(Double.parseDouble(coors[1])),Lon.fromDegrees(Double.parseDouble(coors[2])),1e4);
                	temp2D = new Point2D.Double(Double.parseDouble(coors[1]),Double.parseDouble(coors[2]));
                	
                	tempAOpath.add(temp2D);
                	tempP.add(tempPP);
                	count++;
                }
                
                tempAOpath.add(tail);
                tempP.add(tailP);
                tempAo.setAoCoords(tempAOpath);
                
                
                //New ao coords
                //broken_text = line.split(" ");
                CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), tempAo);
                replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);
                //addedObjectArray[addedObjectsList.getSelectedIndex()].getAo().setAoCoords(tempAOpath);
                wwd.getModel().getLayers().remove(tempAo.getPos());
                addBasicAO(tempP);
            }
        });

        //Final add
        panel.add(aoNameLabel);
        panel.add(aoIDTextField);
        panel.add(opNameLabel);
        panel.add(opNameTextField);
        panel.add(classificationLabel);
        panel.add(classificationTextField);
        panel.add(commentsLabel);
        panel.add(commentsTextArea);
        /*
        panel.add(aoPoint1Label);
        panel.add(latTextField);
        panel.add(lonTextField);
        panel.add(aoPoint2Label);
        panel.add(lat2TextField);
        panel.add(lon2TextField);
        */
        panel.add(applyButton);
        
        masterFrame.remove(rightMasterPanel);
        rightMasterPanel = new JPanel();
        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        rightMasterPanel.add(panel);
        masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
        masterFrame.repaint();
        masterFrame.revalidate();
    }

    private static void createGenericSymbologyDetails(final GenericSymbology genericSymbology) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));

        JLabel aoNameLabel = new JLabel("Symbol ID");
        aoNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField aoIDTextField = new JTextField();
        aoIDTextField.setPreferredSize(new Dimension(150, 25));
        aoIDTextField.setText(genericSymbology.getsID());

        JLabel opNameLabel = new JLabel("Code Name");
        opNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField opNameTextField = new JTextField();
        opNameTextField.setPreferredSize(new Dimension(150, 25));
        opNameTextField.setText(genericSymbology.getCodeName());

        JLabel classificationLabel = new JLabel("Type");
        classificationLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField classificationTextField = new JTextField();
        classificationTextField.setPreferredSize(new Dimension(150, 25));
        classificationTextField.setText(genericSymbology.getType());

        JLabel urgencyLabel = new JLabel("Urgency");
        urgencyLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField urgencyTextField = new JTextField();
        urgencyTextField.setPreferredSize(new Dimension(150, 25));
        urgencyTextField.setText(genericSymbology.getUrgency());

        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setPreferredSize(new Dimension(100, 25));
        final JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setPreferredSize(new Dimension(280, 150));
        commentsTextArea.setText(genericSymbology.getComments());

        JLabel aoPoint1Label = new JLabel("Latitude");
        aoPoint1Label.setPreferredSize(new Dimension(75, 25));
        final JTextField latTextField = new JTextField();
        latTextField.setPreferredSize(new Dimension(50, 25));
        JLabel aoPoint2Label = new JLabel("Longitude");
        aoPoint2Label.setPreferredSize(new Dimension(75, 25));
        final JTextField lonTextField = new JTextField();
        lonTextField.setPreferredSize(new Dimension(50, 25));

        JLabel geoZoneLabel = new JLabel("Geo Zone");
        geoZoneLabel.setPreferredSize(new Dimension(300, 25));
        geoZoneLabel.setHorizontalTextPosition(JLabel.CENTER);
        JLabel geoZoneIDLabel = new JLabel("Geo Zone ID");
        geoZoneIDLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField geoZoneIDTextField = new JTextField();
        geoZoneIDTextField.setPreferredSize(new Dimension(150, 25));
        geoZoneIDTextField.setText(genericSymbology.getGeoZone().getGeoZoneID());
        JLabel geoZoneTypeLabel = new JLabel("Geo Zone Type");
        geoZoneTypeLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField geoZoneTypeTextField = new JTextField();
        geoZoneTypeTextField.setPreferredSize(new Dimension(150, 25));
        geoZoneTypeTextField.setText(genericSymbology.getGeoZone().getType());
        JLabel geoZoneRadius = new JLabel("Radius");
        geoZoneRadius.setPreferredSize(new Dimension(100, 25));
        final JTextField geoZoneRadiusTextField = new JTextField();
        geoZoneRadiusTextField.setPreferredSize(new Dimension(150, 25));
        geoZoneRadiusTextField.setText(Double.toString(genericSymbology.getGeoZone().getradius()));
        JButton geoZoneAddButton = new JButton();
        geoZoneAddButton.setText("Add Geo Zone");
        geoZoneAddButton.setPreferredSize(new Dimension(300, 25));
        
        
        geoZoneAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                GeoZone tempGeoZone = new GeoZone(geoZoneIDTextField.getText(), geoZoneTypeTextField.getText(), genericSymbology.getPosition(), Double.parseDouble(geoZoneRadiusTextField.getText()),genericSymbology.getGeoZone().getPos());
                int pos = placeGeoZone(tempGeoZone.getArea().getLatitude().degrees, tempGeoZone.getArea().getLongitude().degrees, tempGeoZone.getradius());
                tempGeoZone.setPos(pos);
            }
        });

        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Apply Pressed");
                GeoZone ngs = new GeoZone(geoZoneIDTextField.getText(), geoZoneTypeTextField.getText(), genericSymbology.getPosition(), Double.parseDouble(geoZoneRadiusTextField.getText()),genericSymbology.getGeoZone().getPos());
                GenericSymbology tempGS = addedObjectArray[addedObjectsList.getSelectedIndex()].getGenericSymbology();
                tempGS.setsID(aoIDTextField.getText());
                tempGS.setCodeName(opNameTextField.getText());
                tempGS.setType(classificationTextField.getText());
                tempGS.setUrgency(urgencyTextField.getText());
                tempGS.setGeoZone(ngs);
		
                //New coords
                CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), tempGS);
                replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);
            }
        });

        panel.add(aoNameLabel);
        panel.add(aoIDTextField);
        panel.add(opNameLabel);
        panel.add(opNameTextField);
        panel.add(classificationLabel);
        panel.add(classificationTextField);
        panel.add(urgencyLabel);
        panel.add(urgencyTextField);
        panel.add(commentsLabel);
        panel.add(commentsTextArea);
        panel.add(aoPoint1Label);
        panel.add(latTextField);
        panel.add(aoPoint2Label);
        panel.add(lonTextField);
        panel.add(geoZoneLabel);
        panel.add(geoZoneIDLabel);
        panel.add(geoZoneIDTextField);
        panel.add(geoZoneRadius);
        panel.add(geoZoneRadiusTextField);
        panel.add(geoZoneTypeLabel);
        panel.add(geoZoneTypeTextField);
        panel.add(geoZoneAddButton);
        panel.add(applyButton);
        
        masterFrame.remove(rightMasterPanel);
        rightMasterPanel = new JPanel();
        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        rightMasterPanel.add(panel);
        masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
        masterFrame.repaint();
        masterFrame.revalidate();
    }

    private static void createMedicalSymbologyDetails(final MecialSymbology medicalSymbbology) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));

        JLabel aoNameLabel = new JLabel("Symbol ID");
        aoNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField aoIDTextField = new JTextField();
        aoIDTextField.setPreferredSize(new Dimension(150, 25));
        aoIDTextField.setText(medicalSymbbology.getSymbol().getsID());

        JLabel codeNameLabel = new JLabel("Code Name");
        codeNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField codeNameTextField = new JTextField();
        codeNameTextField.setPreferredSize(new Dimension(150, 25));
        codeNameTextField.setText(medicalSymbbology.getSymbol().getCodeName());

        JLabel classificationLabel = new JLabel("Type");
        classificationLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField classificationTextField = new JTextField();
        classificationTextField.setPreferredSize(new Dimension(150, 25));
        classificationTextField.setText(medicalSymbbology.getSymbol().getType());

        JLabel urgencyLabel = new JLabel("Urgency");
        urgencyLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField urgencyTextField = new JTextField();
        urgencyTextField.setPreferredSize(new Dimension(150, 25));
        urgencyTextField.setText(medicalSymbbology.getUrgency());

        JLabel opnameLabel = new JLabel("Operation Name");
        opnameLabel.setPreferredSize(new Dimension(100,25));
        final JTextField opnameTextField = new JTextField();
        opnameTextField.setPreferredSize(new Dimension(150, 25));
        opnameTextField.setText(medicalSymbbology.getSymbol().getOPName());
        
        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setPreferredSize(new Dimension(100, 25));
        final JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setPreferredSize(new Dimension(280, 150));
        commentsTextArea.setText(medicalSymbbology.getComments());

        JLabel aoPoint1Label = new JLabel("Latitude");
        aoPoint1Label.setPreferredSize(new Dimension(75, 25));
        final JTextField latTextField = new JTextField();
        latTextField.setPreferredSize(new Dimension(50, 25));
        JLabel aoPoint2Label = new JLabel("Longitude");
        aoPoint2Label.setPreferredSize(new Dimension(75, 25));
        final JTextField lonTextField = new JTextField();
        lonTextField.setPreferredSize(new Dimension(50, 25));
        latTextField.setText(medicalSymbbology.getSymbol().getPosition().getLatitude().toString());
        lonTextField.setText(medicalSymbbology.getSymbol().getPosition().getLongitude().toString());
        
        
        //export button + actionlistener
        JButton exportButton = new JButton();
        exportButton.setText("Export Metadata");
        exportButton.setPreferredSize(new Dimension(300, 25));
        exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				FileWriter fw;
				String xmlContent = "";
				FileOutputStream fop;
				StringWriter writer = new StringWriter();
				File file = new File("export.xml");
				try {
					//for building xml document
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					
					Document doc = docBuilder.newDocument();
					
					//facility root element
					Element rootElement = doc.createElement("facility");
					doc.appendChild(rootElement);
					
					
					//role
					Attr attr = doc.createAttribute("id");
					attr.setValue("1");
					Element role = doc.createElement("role");
					role.setAttributeNode(attr);
					rootElement.appendChild(role);
					
					//operation
					Element operation = doc.createElement("operation");
					operation.appendChild(doc.createTextNode(medicalSymbbology.getSymbol().getOPName()));
        			role.appendChild(operation);
					
					//name
					Element name = doc.createElement("name");
					name.appendChild(doc.createTextNode(medicalSymbbology.getSymbol().getCodeName()));
					role.appendChild(name);
					
					//for transforming document to xml format
					TransformerFactory transFactory = TransformerFactory.newInstance();
					Transformer transformer = transFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(writer);
					
					transformer.transform(source, result);
					
					System.out.println(writer.toString());

				} catch (Exception e){
					e.printStackTrace();
				}
		
				
				
				
				//specifies file filter to save as xml file only
				FileFilter filter = new FileNameExtensionFilter("XML File", "xml");
				
				//creates file chooser object for saving xml file when exporting
				JFileChooser saveChooser = new JFileChooser();
				saveChooser.setDialogTitle("Specify Save Location");
				saveChooser.setFileFilter(filter);
				
				//user selection as integer
				int userSelection = saveChooser.showSaveDialog(masterFrame);
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					xmlContent = writer.toString();
					File fileToSave = saveChooser.getSelectedFile();
					
					try {
						fw = new FileWriter(fileToSave.getAbsolutePath());
						fw.write(xmlContent.toString());
						fw.flush();
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				}
				
			}
        	
        });
        
        //apply button + action listener
        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.setPreferredSize(new Dimension(300, 25));
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Apply Pressed");
                Angle lat = null;
                Angle lon = null;
                Position pos = medicalSymbbology.getSymbol().getPosition();
                
                //updates all the fields for the currently selected object
                //medicalSymbbology.getSymbol().setPosition(new Position(lat.fromDegrees(Double.parseDouble(latTextField.getText().replace("�, ""))), lon.fromDegrees(Double.parseDouble(lonTextField.getText().replace("�, ""))), pos.getElevation()));
                medicalSymbbology.getSymbol().setsID(aoIDTextField.getText());
                medicalSymbbology.getSymbol().setCodeName(codeNameTextField.getText());
                medicalSymbbology.getSymbol().setType(classificationTextField.getText());
                medicalSymbbology.getSymbol().setOPName(opnameTextField.getText());
                medicalSymbbology.setUrgency(urgencyTextField.getText());
                medicalSymbbology.setComments(commentsTextArea.getText());
                
                //Old Broken Method - made copy of object instead of update details
                /*GenericSymbology tempGS = addedObjectArray[addedObjectsList.getSelectedIndex()].getMedicalSymbology().getSymbol();
                tempGS.setsID(aoIDTextField.getText());
                tempGS.setCodeName(opNameTextField.getText());
                tempGS.setType(classificationTextField.getText());
                tempGS.setUrgency(urgencyTextField.getText());

                MecialSymbology tempMS = addedObjectArray[addedObjectsList.getSelectedIndex()].getMedicalSymbology();
                tempMS.setSymbol(tempGS);
		
                //New coords
                CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), tempMS);
                replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);*/
                
                
            }
        });

        panel.add(aoNameLabel);
        panel.add(aoIDTextField);
        panel.add(codeNameLabel);
        panel.add(codeNameTextField);
        panel.add(classificationLabel);
        panel.add(classificationTextField);
        panel.add(urgencyLabel);
        panel.add(urgencyTextField);
        panel.add(opnameLabel);
        panel.add(opnameTextField);
        panel.add(commentsLabel);
        panel.add(commentsTextArea);
        panel.add(aoPoint1Label);
        panel.add(latTextField);
        panel.add(aoPoint2Label);
        panel.add(lonTextField);
        panel.add(exportButton);
        panel.add(applyButton);
        
        
        masterFrame.remove(rightMasterPanel);
        rightMasterPanel = new JPanel();
        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        rightMasterPanel.add(panel);
        masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
        masterFrame.repaint();
        masterFrame.revalidate();
    }

    private static void createPOIDetails(final POI poi) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));

        JLabel idLabel = new JLabel("POI ID");
        idLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField idTextField = new JTextField();
        idTextField.setPreferredSize(new Dimension(150, 25));
        idTextField.setText(poi.getPoiID());

        JLabel nameLabel = new JLabel("POI Name");
        nameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(150, 25));
        nameTextField.setText(poi.getPoiName());

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField descriptionTextField = new JTextField();
        descriptionTextField.setPreferredSize(new Dimension(150, 25));
        descriptionTextField.setText(poi.getDescription());

        JLabel enemyTacLabel = new JLabel("Enemy Tac");
        enemyTacLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField enemyTacTextField = new JTextField();
        enemyTacTextField.setPreferredSize(new Dimension(150, 25));
        enemyTacTextField.setText(poi.getEnemyTac());

        JLabel casualtyNumberLabel = new JLabel("Casualty Number");
        casualtyNumberLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField casualtyNumberTextField = new JTextField();
        casualtyNumberTextField.setPreferredSize(new Dimension(150, 25));
        casualtyNumberTextField.setText(Integer.toString(poi.getCasualtyNumber()));

        JLabel priorityLabel = new JLabel("Priority");
        priorityLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField priorityTextField = new JTextField();
        priorityTextField.setPreferredSize(new Dimension(150, 25));
        priorityTextField.setText(poi.getPriority());

        JLabel mobilityLabel = new JLabel("Mobility");
        mobilityLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField mobilityTextField = new JTextField();
        mobilityTextField.setPreferredSize(new Dimension(150, 25));
        mobilityTextField.setText(poi.getMobility());
        JLabel casualtiesLabel = new JLabel("Casualties");
        casualtiesLabel.setPreferredSize(new Dimension(100, 25));
        final Casualty[] c = new Casualty[poi.getCasualties().size()];
        int i = 0;
        for (Casualty casualty : poi.getCasualties()) {
            c[i] = casualty;
            i++;
        }
        final JList<Casualty> casualtiesList = new JList<Casualty>(c);
        casualtiesList.setPreferredSize(new Dimension(250, 50));

        JLabel casualtyIDLabel = new JLabel("CasualtyID");
        casualtyIDLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField casualtyIDTextField = new JTextField();
        casualtyIDTextField.setPreferredSize(new Dimension(150, 25));

        JLabel woundsLabel = new JLabel("Wounds");
        woundsLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField woundsTextField = new JTextField();
        woundsTextField.setPreferredSize(new Dimension(150, 25));

        JLabel nationalityLabel = new JLabel("Nationality");
        nationalityLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField nationalityTextField = new JTextField();
        nationalityTextField.setPreferredSize(new Dimension(150, 25));

        //Bug: Only 1 casualty can be added per apply button click
        final JButton addCasualtyButton = new JButton();
        addCasualtyButton.setPreferredSize(new Dimension(300, 25));
        addCasualtyButton.setText("Add Casualty");
        final Casualty[] tc = new Casualty[c.length + 1];
        System.arraycopy(c, 0, tc, 0, c.length);
        addCasualtyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                tc[tc.length - 1] = new Casualty(casualtyIDTextField.getText(), woundsTextField.getText(), nationalityTextField.getText());
                casualtiesList.setListData(tc);
            }
        });
        JLabel urgencyLabel = new JLabel("Urgency");
        urgencyLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField urgencyTextField = new JTextField();
        urgencyTextField.setPreferredSize(new Dimension(150, 25));
        urgencyTextField.setText(poi.getUrgency());

        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setPreferredSize(new Dimension(100, 25));
        final JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setPreferredSize(new Dimension(280, 100));
        commentsTextArea.setText(poi.getComments());

        JLabel aoPoint1Label = new JLabel("Latitude");
        aoPoint1Label.setPreferredSize(new Dimension(75, 25));
        final JTextField latTextField = new JTextField();
        latTextField.setPreferredSize(new Dimension(50, 25));
        JLabel aoPoint2Label = new JLabel("Longitude");
        aoPoint2Label.setPreferredSize(new Dimension(75, 25));
        final JTextField lonTextField = new JTextField();
        lonTextField.setPreferredSize(new Dimension(50, 25));

        JButton applyButton = new JButton();
        applyButton.setPreferredSize(new Dimension(300, 25));
        applyButton.setText("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Apply Pressed");
                POI tempPoi = addedObjectArray[addedObjectsList.getSelectedIndex()].getPoi();

                tempPoi.setPoiID(idTextField.getText());
                tempPoi.setDescription(descriptionTextField.getText());
                tempPoi.setCasualtyNumber(Integer.parseInt((casualtyNumberTextField.getText())));
                tempPoi.setPoiName(nameTextField.getText());
                tempPoi.setEnemyTac(enemyTacTextField.getText());
                tempPoi.setPriority(priorityTextField.getText());
                tempPoi.setMobility(mobilityTextField.getText());
                ArrayList<Casualty> ncl = new ArrayList<Casualty>();
                for (Casualty casualty : tc) {
                    ncl.add(casualty);
                }
                tempPoi.setCasualties(ncl);
                tempPoi.setUrgency(urgencyTextField.getText());
                tempPoi.setComments(commentsTextArea.getText());

		//New coords
                CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), tempPoi);
                replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);
            }
        });
        
        panel.add(idLabel);
        panel.add(idTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(descriptionLabel);
        panel.add(descriptionTextField);
        panel.add(enemyTacLabel);
        panel.add(enemyTacTextField);
        panel.add(casualtyNumberLabel);
        panel.add(casualtyNumberTextField);
        panel.add(priorityLabel);
        panel.add(priorityTextField);
        panel.add(mobilityLabel);
        panel.add(mobilityTextField);
        panel.add(casualtiesLabel);
        panel.add(casualtiesList);
        panel.add(casualtyIDLabel);
        panel.add(casualtyIDTextField);
        panel.add(woundsLabel);
        panel.add(woundsTextField);
        panel.add(nationalityLabel);
        panel.add(nationalityTextField);
        panel.add(addCasualtyButton);
        panel.add(urgencyLabel);
        panel.add(urgencyTextField);
        panel.add(commentsLabel);
        panel.add(commentsTextArea);
        panel.add(aoPoint1Label);
        panel.add(latTextField);
        panel.add(aoPoint2Label);
        panel.add(lonTextField);
        panel.add(applyButton);
        
        masterFrame.remove(rightMasterPanel);
        rightMasterPanel = new JPanel();
        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        rightMasterPanel.add(panel);
        masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
        masterFrame.repaint();
        masterFrame.revalidate();
    }

    /** Method for generating and populating right-hand panel for route selected in left-hand panel
     * 
     * @param route
     */
    //Method for populating right panel with details for selected route
    private static void createRouteDetails(final Route route) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));

        JLabel pathNameLabel = new JLabel("Path Name");
        pathNameLabel.setPreferredSize(new Dimension(100, 25));
        final JTextField pathNameTextField = new JTextField();
        pathNameTextField.setPreferredSize(new Dimension(150, 25));
        pathNameTextField.setText(route.getPathName());

        
        //copies positions from current route into array of positions
        final Position[] p = new Position[route.getPathCoords().size()];
        int i = 0;
        for (Position position : route.getPathCoords()) {
            p[i] = route.getPathCoords().get(i);
            i++;
        }
        
        
        
        final JTextField latTextField = new JTextField();
        latTextField.setPreferredSize(new Dimension(140, 25));
        final JTextField lonTextField = new JTextField();
        lonTextField.setPreferredSize(new Dimension(140, 25));

        final JList<Position> routePointsList = new JList<Position>(p);
        routePointsList.setPreferredSize(new Dimension(250, 400));
        routePointsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                try {
                    latTextField.setText(p[routePointsList.getSelectedIndex()].getLatitude().toString());
                    lonTextField.setText(p[routePointsList.getSelectedIndex()].getLongitude().toString());
                } catch (IndexOutOfBoundsException e) {

                }
            }
        });

        //apply button 
        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.setPreferredSize(new Dimension(300, 25));
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                System.out.println("Apply Pressed");
                
                //OLD Method - created a copy of selected route with modified positions instead of changing current positions
                Route tempRoute = addedObjectArray[addedObjectsList.getSelectedIndex()].getRoute();
                /*tempRoute.setPathName(pathNameTextField.getText());
                //TODO: fix this from making a "NEW" route
                p[routePointsList.getSelectedIndex()] = Position.fromDegrees(Double.parseDouble(latTextField.getText().replace("�, "")), Double.parseDouble(lonTextField.getText().replace("�, "")), p[routePointsList.getSelectedIndex()].getAltitude());
                routePointsList.setListData(p);
                ArrayList<Position> tp = new ArrayList<Position>();
                for (Position position : p) {
                    tp.add(position);
                }
                tempRoute.setPathCoords(tp);*/

                //TODO: Instead of copying route - call setPathCoords() with new Coords. :)
                ArrayList<Position> newCoords = new ArrayList<Position>();
                
               // p[routePointsList.getSelectedIndex()] = Position.fromDegrees(Double.parseDouble(latTextField.getText().replace("�, "")), Double.parseDouble(lonTextField.getText().replace("�, "")), p[routePointsList.getSelectedIndex()].getAltitude());
                routePointsList.setListData(p);
                for (Position pos : p) {
                	newCoords.add(pos);
                }
                
                route.setPathCoords(newCoords);
                                
                
                //New path
                CreatedObject newCO = new CreatedObject(addedObjectArray[addedObjectsList.getSelectedIndex()].getObjectType(), route);
                replaceObjectInAddedList(addedObjectsList.getSelectedIndex(), newCO);

                //New route
                //Adds a modified route to the map
                System.out.println("Added new route");
                wwd.getModel().getLayers().remove(tempRoute);
                /*Route newRoute = new Route("New Path", tp, 0, 0);
                addBasicPath(newRoute.getPathCoords());
                pathPositions = new ArrayList<Position>();
                addObjectToAddedList("Route", route);*/
            }
        });

        
        //create jlabel for displaying overall length of route
        LengthMeasurer lm = new LengthMeasurer(route.getPathCoords());
        double rl = (lm.getLength(wwd.getModel().getGlobe()) / 1000);
        DecimalFormat df = new DecimalFormat("#.00");
        JLabel routeLengthLabel = new JLabel("Route Length : " + df.format(rl).toString() + "km");
        
        panel.add(pathNameLabel);
        panel.add(pathNameTextField);
        panel.add(routePointsList);
        panel.add(latTextField);
        panel.add(lonTextField);
        panel.add(routeLengthLabel);
        panel.add(applyButton);
        
        masterFrame.remove(rightMasterPanel);
        rightMasterPanel = new JPanel();
        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        rightMasterPanel.add(panel);
        masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
        masterFrame.repaint();
        masterFrame.revalidate();
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    // ------------------------------------------------------------------------------------
    private static void createAndShowGUI() {
        // Make sure we have nice window decorations.
        //JFrame.setDefaultLookAndFeelDecorated(true);
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {}
    	 
        // Create and set up the window.
        masterFrame = new JFrame("SAAB Medical Support Decision System");
        masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        Menus menu = new Menus();
        masterFrame.setJMenuBar(menu.createMenuBar());
        masterFrame.setContentPane(menu.createContentPane());

        // Display the window.
        masterFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
	


	BasicScheduledTaskService threatCheck = new BasicScheduledTaskService();
        
        //anonymous inner class for checking threat/route collisions every 5 seconds
        final class CheckThreat implements Runnable {

			@Override
			public void run() {
				checkThreatAreas();
				
			}
        	
        }
        
        threatCheck.addRepeatingTask(new CheckThreat(), 5000, 5000, TimeUnit.MILLISECONDS);


        //The options menu is used to toggle on and off ui components
        JMenu optionsMenu = new JMenu("Options");

        //World map display option
        JMenuItem worldMap = new JMenuItem("Show/Hide World Map");
        worldMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (worldMapEnabled == true) {
                    //Disable world map
                    wwd.getModel().getLayers().getLayerByName("World Map").setEnabled(false);
                    worldMapEnabled = false;
                } else {
                    //Show world map
                    wwd.getModel().getLayers().getLayerByName("World Map").setEnabled(true);
                    worldMapEnabled = true;
                }
            }
        });
        optionsMenu.add(worldMap);

        //Map control options
        JMenuItem mapControls = new JMenuItem("Show/Hide Map Controls");
        mapControls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (viewControlsEnabled == true) {
                    wwd.getModel().getLayers().getLayerByName("View Controls").setEnabled(false);
                    viewControlsEnabled = false;
                } else {
                    wwd.getModel().getLayers().getLayerByName("View Controls").setEnabled(true);
                    viewControlsEnabled = true;
                }

            }
        });
        optionsMenu.add(mapControls);

        //Compass option
        JMenuItem compass = new JMenuItem("Show/Hide Compass");
        compass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (compassEnabled == true) {
                    wwd.getModel().getLayers().getLayerByName("Compass").setEnabled(false);
                    compassEnabled = false;
                } else {
                    wwd.getModel().getLayers().getLayerByName("Compass").setEnabled(true);
                    compassEnabled = true;
                }
            }
        });
        optionsMenu.add(compass);

        //Scale Option
        JMenuItem scale = new JMenuItem("Show/Hide Scale");
        scale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (scaleEnabled == true) {
                    wwd.getModel().getLayers().getLayerByName("Scale bar").setEnabled(false);
                    scaleEnabled = false;
                } else {
                    wwd.getModel().getLayers().getLayerByName("Scale bar").setEnabled(true);
                    scaleEnabled = true;
                }
            }
        });
        optionsMenu.add(scale);

        //MGRS Option
        JMenuItem mgrsOption = new JMenuItem("Show/Hide MGRS");
        mgrsOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (mgrsEnabled == true) {
                    wwd.getModel().getLayers().getLayerByName("MGRS Layer").setEnabled(false);
                    mgrsEnabled = false;
                } else {
                    wwd.getModel().getLayers().getLayerByName("MGRS Layer").setEnabled(true);
                    mgrsEnabled = true;
                }
            }
        });
        optionsMenu.add(mgrsOption);

        //Latitude / Longtitude Option
        JMenuItem latLonOption = new JMenuItem("Show/Hide LatLon");
        latLonOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (latLonEnabled == true) {
                    wwd.getModel().getLayers().getLayerByName("LatLon Layer").setEnabled(false);
                    latLonEnabled = false;
                } else {
                    wwd.getModel().getLayers().getLayerByName("LatLon Layer").setEnabled(true);
                    latLonEnabled = true;
                }
            }
        });
        optionsMenu.add(latLonOption);

        masterFrame.getJMenuBar().add(optionsMenu);

        // Loading the world wind map
        wwd = new WorldWindowGLJPanel();
        wwd.setPreferredSize(new java.awt.Dimension(800, 800));
        masterFrame.getContentPane().add(wwd, java.awt.BorderLayout.CENTER);
        wwd.setModel(new BasicModel());

        
        splash.addRenderingListener(wwd);
                
        //Annotation Layer
        annotationlayer = new AnnotationLayer();
        wwd.getModel().getLayers().add(annotationlayer);
        
        //Renderable Layer
        routelayer = new RenderableLayer();
        wwd.getModel().getLayers().add(routelayer);
        
        //View control layer
        ViewControlsLayer viewControls = new ViewControlsLayer();
        viewControls.setShowFovControls(true);
        viewControls.setShowHeadingControls(true);
        viewControls.setShowLookControls(true);
        viewControls.setShowPanControls(true);
        viewControls.setShowPitchControls(true);
        viewControls.setShowVeControls(true);
        viewControls.setShowZoomControls(true);
        viewControls.setEnabled(true);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd, viewControls));
        wwd.getModel().getLayers().add(viewControls);

        MGRSGraticuleLayer mgrsLayer = new MGRSGraticuleLayer();
        mgrsLayer.setName("MGRS Layer");
        wwd.getModel().getLayers().add(mgrsLayer);
        wwd.getModel().getLayers().getLayerByName("MGRS Layer").setEnabled(false);

        LatLonGraticuleLayer latLonLayer = new LatLonGraticuleLayer();
        latLonLayer.setName("LatLon Layer");
        wwd.getModel().getLayers().add(latLonLayer);
        wwd.getModel().getLayers().getLayerByName("LatLon Layer").setEnabled(false);

        rightMasterPanel.setPreferredSize(new Dimension(300, 800));
        masterFrame.getContentPane().add(setupBottomBar(), BorderLayout.SOUTH);
        masterFrame.getContentPane().add(createLeftPanel(), BorderLayout.WEST);
        masterFrame.getContentPane().add(rightMasterPanel, BorderLayout.EAST);

        masterFrame.setVisible(true);
        masterFrame.pack();
        masterFrame.setLocationRelativeTo(null);

        try {
            wwd.addSelectListener(new BasicDragger(wwd));
            //This is a listener for the enter key when finishing up adding a new route
            wwd.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent evt) {
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (placingType == 4) {
                            // Finished drawing route so now place route							
                            System.out.println("Done with placing path");
                            Route route = new Route("New Path", pathPositions, 0, 0,-1, null);
                            //
                            route.setPolyline(route.getPathCoords(), 300);
                            MeasureTool measure = new MeasureTool(wwd);
                    		measure.setController(new MeasureToolController());
                    		measure.getUnitsFormat().setLengthUnits(UnitsFormat.KILOMETERS);
                            route.getPolyline().setColor(Color.RED);
                            route.getPolyline().setLineWidth(100);
                            route.getPolyline().setFollowTerrain(true);
                            measure.setMeasureShape(route.getPolyline());
                    		
                            //int pos = addBasicPath();
                            //route.setPos(pos);
                            pathPositions = new ArrayList<Position>();
                            //addBasicPath(pathPositions);
                            
                            route.setPos(wwd.getModel().getLayers().size()-1);
                            addObjectToAddedList("Route", route);
                            addedObjectsList.validate();
                            annotationlayer.removeAllAnnotations();
                            
                            placingType = 0;
                        }
                        System.out.println("Pressed");
                        
                      //Tao's part 
                        if (placingType == 2) {
                        	System.out.println("enter for AO pressed");
							if(pathPositions.size() > 2){
								AOcount++;
								pathPositions.add(Position.fromDegrees(
										pathPositions.get(0).getLatitude()
												.getDegrees(), pathPositions.get(0)
												.getLongitude().getDegrees(), 1e4));
								int layerpos = addBasicAO(pathPositions);
								
								ArrayList<Point2D> temp = new ArrayList<Point2D>();
								Point2D temp2D;
								for(int i = 0; i<pathPositions.size();i++){
									temp2D =  new Point2D.Double(pathPositions.get(i).getLatitude().getDegrees(),pathPositions.get(i).getLongitude().getDegrees());
									temp.add(i, temp2D);
									//temp2D = new Point2D(pathPositions.get(i).getLatitude().getDegrees(),pathPositions.get(i).getLatitude().getDegrees());
									//Point2D tempPoint = new Point2D(pathPositions.get(i).getLatitude().getDegrees(),pathPositions.get(i).getLongitude().getDegrees());
									
									//temp.add(pathPositions.get(i).getLatitude(),pathPositions.get(i).getLongitude());
								}
								String AOname = "New AO " + AOcount;
								AO ao = new AO(AOname, 0, 0,temp , "New AO", "", "",layerpos);
								addObjectToAddedList("AO", ao);
								
								/*
								for(int i = 0; i<ao.getAoCoords().size();i++){
									System.out.println("here is the pos");
									System.out.println(ao.getAoCoords().get(i).getX());

									System.out.println(ao.getAoCoords().get(i).getY());
									
									//temp2D = new Point2D(pathPositions.get(i).getLatitude().getDegrees(),pathPositions.get(i).getLatitude().getDegrees());
									//Point2D tempPoint = new Point2D(pathPositions.get(i).getLatitude().getDegrees(),pathPositions.get(i).getLongitude().getDegrees());
									
									//temp.add(pathPositions.get(i).getLatitude(),pathPositions.get(i).getLongitude());
								}
								*/
								pathPositions = new ArrayList<Position>();
								placingType = 0;
								System.out.println("Done with choosing ao");
								annotationlayer.removeAllAnnotations();
							}
						} else
                        	pathPositions = new ArrayList<Position>();//empty the array if points are not >2
                   
                    }
                        
                }
            });
            
            
            
            
            //This will consume mouse events while placing objects onto the map so that the map doesn't move with a single click
            wwd.getInputHandler().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 1
                            && mouseEvent.getButton() == MouseEvent.BUTTON1
                            && mouseEvent.getModifiersEx() == 0) {
                        if (placingType != 0) {
                            System.out.println("Consumed");
                            mouseEvent.consume();
                        }
                    }
                }
            });

            //Updates the coodinates at the bottom of the screen for the mouse position
            //Note other things could be added here such as tool use descriptions
            wwd.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent arg0) {
                    try {
                        Position mousePosition = wwd.getCurrentPosition();
                        double latitude = mousePosition.getLatitude().getDegrees();
                        double longitude = mousePosition.getLongitude().getDegrees();
                        latLabel.setText("Lat:" + Double.toString(latitude));
                        lonLabel.setText("Lon:" + Double.toString(longitude));
                    } catch (IndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {

                    }
                    masterFrame.validate();
                }

                //Mouse drag option
                @Override
                public void mouseDragged(MouseEvent arg0) {
                }
            });

            //Will do the appropiate action when clicking on the map
            //All of the object adding is done on these clicks
            wwd.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Get clicked position on the globe.
                    Position clickedPosition = wwd.getCurrentPosition();
                    double latitude = clickedPosition.getLatitude().getDegrees();
                    double longitude = clickedPosition.getLongitude().getDegrees();
                    double altitude = clickedPosition.getAltitude();
                    double elevation = clickedPosition.getElevation();

                    System.out.println("Clicked Latitude: " + latitude);
                    System.out.println("Clicked Longitude: " + longitude);

                    // Create and place symbol class
                    if (placingType == 1) {
                        System.out.println("Placing");
                        int pos =  wwd.getModel().getLayers().size();
                        addTacticalSymbols(latitude, longitude, elevation, selectedMenuPath, "MIL-STD-2525 Tactical Symbol", "My Symbol Layer");
                        System.out.println("Now placing new symbol = " + selectedMenuPath);
                        GenericSymbology genericSymbology = new GenericSymbology("New Generic Symbology", "", selectedMenuPath, .5, 1, true, "", "", Position.fromDegrees(latitude, longitude, altitude), new GeoZone("", "", Position.fromDegrees(0, 0), 0,addedObjectArray.length),pos);
                        addObjectToAddedList("GenericSymbology", genericSymbology);
                        annotationlayer.removeAllAnnotations();
                        placingType = 0;
                    } else if (placingType == 6) {
                    	
                        System.out.println("Placing");
                        int pos =  wwd.getModel().getLayers().size();
                        addTacticalSymbols(latitude, longitude, elevation, selectedMenuPath, "MIL-STD-2525 Tactical Symbol", "My Symbol Layer");
                        GenericSymbology genericSymbology = new GenericSymbology("New Generic Symbology", "", selectedMenuPath, .5, 1, true, "", "", Position.fromDegrees(latitude, longitude, altitude), new GeoZone("", "", Position.fromDegrees(0, 0), 0,addedObjectArray.length),-1);
                        MecialSymbology medicalSymbology = new MecialSymbology(new ArrayList<Specialty>(), genericSymbology, "", "",pos);
                        addObjectToAddedList("MedicalSymbology", medicalSymbology);
                        
                        System.out.println("Selected menu = " + selectedMenuPath);
                        annotationlayer.removeAllAnnotations();
                        placingType = 0;
                    } 
                    // Create and place ao class
                    else if (placingType == 2) {
                    	// First corner
						if (pathPositions.size() == 0) {
							System.out.println(pathPositions.size());
							// Reset array
							pathPositions = new ArrayList<Position>();
							pathPositions.add(Position.fromDegrees(latitude,
									longitude, 1e4));
							System.out.println(pathPositions.size());
							System.out.println("Adding first Point");
						}
						// Others
						else {
							pathPositions.add(Position.fromDegrees(latitude,
									longitude, 1e4));
							System.out.println("point add");
							System.out.println(pathPositions.size());
						}
                    } 
                    // Create and place Geo zone class
                    else if (placingType == 3) {
                        int pos = placeGeoZone(latitude, longitude, 50000);
                        GeoZone geoZone = new GeoZone("New GeoZone", "", Position.fromDegrees(latitude, longitude), 50000.0,pos);
                        addObjectToAddedList("GeoZone", geoZone);
                        System.out.println(addedObjectArray.length);
                        placingType = 0;
                    } 
                    
                    // Create and place route class
                    else if (placingType == 4) {
                        // First corner
                    	Polyline routepolyline = new Polyline();
               	        //lineBuilder = new LineBuilder(wwd, routelayer, routepolyline);        
                        if (pathPositions.size() == 0) {
                            // Reset array
                            pathPositions = new ArrayList<Position>();
                            pathPositions.add(Position.fromDegrees(latitude, longitude, 0));
                            System.out.println("Adding First Point");
                        } 
                        // Other points
                        else if (pathPositions.size() >= 1) {
                            pathPositions.add(Position.fromDegrees(latitude, longitude, 0));
                            System.out.println("Adding New Point");
                        }
                        

                    } 
                    // Create and place casualty
                    else if (placingType == 5) {
                    	int pos =  wwd.getModel().getLayers().size();
                        addTacticalSymbols(latitude, longitude, altitude, "SFXPxxxxxx--xxG", "POI", "POI Layer");
                        POI poi = new POI("New POI", "New POI", "", "", 0, "", "", new ArrayList<Casualty>(), "", "",pos);
                        addObjectToAddedList("POI", poi);
                        
                    }
		    // Create and place Airspace BOX
                    else if (placingType == PlacementType.AIRSPACE_BOX) {
                    	
                    	if (pathPositions.size() == 0) {
                    		pathPositions = new ArrayList<Position>();
                    		pathPositions.add(Position.fromDegrees(latitude, longitude));
                            System.out.println("Adding First Position");
                    	} else if (pathPositions.size() >= 1) {
                    		pathPositions.add(Position.fromDegrees(latitude, longitude));
                    		System.out.println("Adding New Point");
                    	}

                    	//placingType = 0;
                    	//addAirspace(PlacementType.AIRSPACE_BOX, pathPositions);
                    	
                    	annotationlayer.removeAllAnnotations();
                	}
                    // Create and place Airspace CYLINDER
                    else if (placingType == PlacementType.AIRSPACE_CYLINDER) { 
                    	ArrayList<Position> pos = new ArrayList<Position>();
                    	pos.add(clickedPosition);
                    	placingType = 0;
                    	addAirspace(PlacementType.AIRSPACE_CYLINDER, pos);
                    	
                    	annotationlayer.removeAllAnnotations();
                	} else {
                        System.out.println("Nothing to place");
                    }
                    
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/** Method for adding airspaces
     * 
     * @param type
     * @param pos
     */
    
    public static void addAirspace(int type, ArrayList<Position> pos) {
    	RenderableLayer layer = new RenderableLayer();
    	layer.setName("Airspace Layer");
    	
    	
    	AirspaceAttributes attr = new BasicAirspaceAttributes();
		attr.setOutlineMaterial(new Material(WWUtil.makeRandomColor(null)));
		attr.setOutlineWidth(2d);
		attr.setOpacity(0.5);
		
    	switch (type) {
    	case PlacementType.AIRSPACE_BOX:
    		
    		Polygon airPoly = new Polygon(pos);
    		airPoly.setAttributes(attr);
    		airPoly.setAltitudes(500, 1500);

    		layer.addRenderable(airPoly);
    		
    		AirSpace boxSpace = new AirSpace(type, pos, 500, 1500);
    		addObjectToAddedList("AirSpace", boxSpace);
    		
    		wwd.getModel().getLayers().add(layer);
    		wwd.updateUI();
    		
    		break;
    	case PlacementType.AIRSPACE_CYLINDER:
    		Position cylinderPos = pos.get(0);
    		LatLon cylinderLatLon = new LatLon(cylinderPos.latitude, cylinderPos.longitude);
    		//take in first item in list as only single position is required
    		//cylinder(pos,height,radius)
    		CappedCylinder airCylinder = new CappedCylinder(cylinderLatLon, 3000);
    		
    		//altitudes(lower, upper)
    		airCylinder.setAltitudes(500, 1500);
    		airCylinder.setAttributes(attr);
    		layer.addRenderable(airCylinder);
    		
    		AirSpace cylinderSpace = new AirSpace(type,pos,500,1500);
    		addObjectToAddedList("AirSpace",cylinderSpace);
    		
    		wwd.getModel().getLayers().add(layer);
    		wwd.updateUI();
    		
    		break;
    	}
		
	}
    
    //Method for measuring length of routes in specified units
    public static double measureRoute(ArrayList<Position> positions) {
		MeasureTool measure = new MeasureTool(wwd);
		
		measure.setController(new MeasureToolController());
		measure.getUnitsFormat().setLengthUnits(UnitsFormat.KILOMETERS);
		measure.setFollowTerrain(true);
	
		Polyline line = new Polyline(positions);
		//line.setLineWidth(100);
		//line.setColor(Color.RED);
		
		measure.setMeasureShape(line);
		measure.setShowAnnotation(true);
		
		System.out.println("MEASUREMENT = " + measure.getLength()/1000 + " km ");

		
		return measure.getLength();
	}

	//Main method of the application that will be invoked when the project is run
    public static void main(String[] args) {
    	
    	//Creates splash screen - hides when map is finished loading
    	splash = new SplashScreen();
    	
    	
    	// Schedule a job for the event-dispatching thread:
    	// Creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Loading Symbology Link File...");
                populateSymbolLinks();
                System.out.println("Loading Menu Files...This may take a while");
                createAndShowGUI();
                MeasureToolController f = new MeasureToolController();
                f.getMeasureTool();
                System.out.println("Loading GUI...");
            }
        });
    }

    /**
     * opens textfile and store items into array object
     *
     * @param fileName
     * @return String array of menus
     */
    private String[] openMenusTxt(String fileName) {
        String[] arrayObj = new String[1];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int i = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 2).equals("//")) {
                    arrayObj = (String[]) resizeArray(arrayObj, i + 1); // resize the array to size+1
                    arrayObj[i] = new String(line.toString());
                    i++;
                }
            }
            reader.close();

        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.");
            e.printStackTrace();
            return null;
        }
        return arrayObj;
    }

    /**
     * adding menu items to its parent menu
     *
     * @param menuName
     * @param menuItem
     * @param handler
     * @param itemNames
     * @return the submenu item
     */
    private JMenu createMenu(JMenu menuName, String menuItem,
            ActionListener handler, String[] itemNames) {
        menuName = new JMenu(menuItem);
        for (String itemName : itemNames) {
            if (itemName != null) {
                if (itemName.substring(0, 1).toString().equals("*")) {
                    JMenu item = new JMenu(itemName.substring(1, itemName.length()));
                    menuName.add(item);
                } else {
                    JMenuItem item = new JMenuItem(itemName);
                    item.addActionListener(handler);
                    menuName.add(item);
                }
            }
        }
        return menuName;
    }

    /**
     * resizing the object array that holds the menu item found in the textfile
     * this is used by openMenusTxt method
     *
     * @param oldArrayy
     * @param newSize
     * @return the new array ojbect
     */
    private static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    /**
     * Adding non submenu items
     *
     * @param menuLevel
     * @param itemMenu
     */
    private void addNonSubMenuItem(JMenu menuLevel, String itemMenu) {
        if (menuLevel != null) {
            menuLevel.remove(0);
        }
        JMenuItem item = new JMenuItem(itemMenu.toString());
        if (!itemMenu.substring(0, 1).equals("-")) {
            menuLevel.addSeparator();
        }
        menuLevel.add(item);
        if (itemMenu.substring(0, 1).equals("-")) {
            item.setEnabled(false);
            menuLevel.addSeparator();
        } else {
            item.addActionListener(this);
        }
    }

    /**
     * adding menu items with submenus
     *
     * @param parentMenu
     * @param menuLevel
     * @param menuItem
     * @param handler
     * @param itemNames
     * @return level of submenu
     */
    private JMenu addSubMenuItem(JMenu parentMenu, JMenu menuLevel,
            String menuItem, ActionListener handler, String[] itemNames) {
        if (parentMenu != null) {
            parentMenu.remove(0);
        }

        menuLevel = createMenu(menuLevel, menuItem, this, itemNames);
        parentMenu.add(menuLevel);
        return menuLevel;
    }

    //Adds object to the object list on the left
    private static int addObjectToAddedList(String name, Object object) {
        CreatedObject[] tempArray = addedObjectArray;
        addedObjectArray = new CreatedObject[addedObjectArray.length + 1];
        System.arraycopy(tempArray, 0, addedObjectArray, 0, addedObjectArray.length - 1);
        addedObjectArray[addedObjectArray.length - 1] = new CreatedObject(name, object);

        addedObjectsList.setListData(addedObjectArray);
        return addedObjectArray.length - 1;
    }

    //Replaces an object to in object list on the left
    private static void replaceObjectInAddedList(int index, CreatedObject object) {
        CreatedObject[] tempArray = addedObjectArray;
        addedObjectArray = new CreatedObject[addedObjectArray.length + 1];
        System.arraycopy(tempArray, 0, addedObjectArray, 0, addedObjectArray.length - 1);
        addedObjectArray[index] = object;

        addedObjectsList.setListData(addedObjectArray);
    }

    //Calls the correct details view when invoked with a generic object
    private static void setupDetailsView(CreatedObject object) {
        System.out.println(object.toString());
        if (object.getObjectType() == "AO") {
            System.out.println("setup ao details");
            createAODetails(object.getAo());
            masterFrame.validate();
        } else if (object.getObjectType() == "GeoZone") {
            System.out.println("setup geozone details");
            //createRightPanel("GeoZone");
            masterFrame.validate();
        } else if (object.getObjectType() == "Route") {
            System.out.println("setup route details");
            createRouteDetails(object.getRoute());
            masterFrame.validate();
        } else if (object.getObjectType() == "GenericSymbology") {
            System.out.println("setup genericsymbology details");
            createGenericSymbologyDetails(object.getGenericSymbology());
            masterFrame.validate();
        } else if (object.getObjectType() == "MedicalSymbology") {
            System.out.println("setup medicalsymbology details");
            createMedicalSymbologyDetails(object.getMedicalSymbology());
            masterFrame.validate();
        } else if (object.getObjectType() == "POI") {
            System.out.println("setup poi details");
            createPOIDetails(object.getPoi());
            masterFrame.validate();
        } else {
            //Nothing selected remove details view
            masterFrame.remove(rightMasterPanel);
            rightMasterPanel = new JPanel();
            rightMasterPanel.setPreferredSize(new Dimension(300, 800));
            masterFrame.add(rightMasterPanel, java.awt.BorderLayout.EAST);
            masterFrame.repaint();
            masterFrame.revalidate();
        }
    }

    //Sets up the coordinates system at the bottom of the screen
    private static JPanel setupBottomBar() {
        JPanel bottomBar = new JPanel();
        bottomBar.setPreferredSize(new Dimension(500, 25));
        latLabel = new JLabel();
        latLabel.setPreferredSize(new Dimension(150, 20));
        lonLabel = new JLabel();
        lonLabel.setPreferredSize(new Dimension(150, 20));
        bottomBar.add(latLabel);
        bottomBar.add(lonLabel);
        return bottomBar;
    }

    ///currently unused bu is an advanced form of path placement
    public void makePanel(String typeString, Dimension size) {
        lineBuilder = new LineBuilder(wwd, null, null);
        lineBuilder.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                fillPointsPanel();
            }
        });
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        newButton = new JButton("Start");
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                lineBuilder.clear();
                lineBuilder.setArmed(true);
                pauseButton.setText("Pause");
                endButton.setText("Done");
                pauseButton.setEnabled(true);
                endButton.setEnabled(true);
                newButton.setEnabled(false);
                ((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        buttonPanel.add(newButton);
        newButton.setEnabled(true);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                lineBuilder.setArmed(!lineBuilder.isArmed());
                pauseButton.setText(!lineBuilder.isArmed() ? "Resume" : "Pause");
                ((Component) wwd).setCursor(Cursor.getDefaultCursor());
            }
        });
        buttonPanel.add(pauseButton);
        pauseButton.setEnabled(false);

        endButton = new JButton("Cancel");
        endButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                lineBuilder.setArmed(false);
                newButton.setEnabled(true);
                pauseButton.setEnabled(false);
                pauseButton.setText("Pause");
                endButton.setEnabled(false);
                outerPanel.setVisible(false);

                ((Component) wwd).setCursor(Cursor.getDefaultCursor());

            }
        });
        buttonPanel.add(endButton);
        endButton.setEnabled(true);

        JPanel pointPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        pointPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.pointLabels = new JLabel[20];
        for (int i = 0; i < this.pointLabels.length; i++) {
            this.pointLabels[i] = new JLabel("");
            pointPanel.add(this.pointLabels[i]);
        }

        // Put the point panel in a container to prevent scroll panel from stretching the vertical spacing.
        JPanel dummyPanel = new JPanel(new BorderLayout());
        dummyPanel.add(pointPanel, BorderLayout.WEST);

        // Put the point panel in a scroll bar.
        JScrollPane scrollPane = new JScrollPane(dummyPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        if (size != null) {
            scrollPane.setPreferredSize(size);
        }

        // Add the buttons, scroll bar and inner panel to a titled panel that will resize with the main window.
        outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder(typeString)));
        outerPanel.setToolTipText("Line control and info");
        outerPanel.add(buttonPanel, BorderLayout.NORTH);
        outerPanel.add(scrollPane, BorderLayout.CENTER);
        outerPanel.setPreferredSize(new Dimension(300, 700));

        rightMasterPanel.add(outerPanel, BorderLayout.WEST);
        wwd.setPreferredSize(new java.awt.Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 270, Toolkit.getDefaultToolkit().getScreenSize().height));
        rightMasterPanel.validate();
    }

    private void fillPointsPanel() {
        int i = 0;
        for (Position pos : lineBuilder.getLine().getPositions()) {
            if (i == this.pointLabels.length) {
                break;
            }

            String las = String.format("Lat %7.4f\u00B0", pos.getLatitude().getDegrees());
            String los = String.format("Lon %7.4f\u00B0", pos.getLongitude().getDegrees());
            pointLabels[i++].setText(las + "  " + los);
        }
        for (; i < this.pointLabels.length; i++) {
            pointLabels[i++].setText("");
        }
    }

    //Populates a map that stores symbols real names and their code
    private static void populateSymbolLinks() {
        String symbolLinkFile = "symbolLinkFile.txt";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        map = new HashMap<String, String>();
        try {
            br = new BufferedReader(new FileReader(symbolLinkFile));
            while ((line = br.readLine()) != null) {
                String[] symbol = line.split(cvsSplitBy);
                map.put(symbol[0], symbol[1]);
                System.out.println(symbol[0] + "," + symbol[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Calculates a symbol code from a menu path that is selected
    private static String getSymbolLink() {
        System.out.println("Beginning seperation");
        String symbolLink = "S";
        String line = "";
        String cvsSplitBy = ",";
        line = selectedMenuPath2;
        String[] symbol = line.split(cvsSplitBy);
        for (String string : symbol) {
            System.out.println(string);
            if (map.get(string) != null) {
                symbolLink = symbolLink + map.get(string);
                System.out.println("SymbolLink=" + symbolLink);
            }
        }
        return symbolLink;
    }
}
