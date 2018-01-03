package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.alpano.gui.UserParameter.CENTER_AZIMUTH;
import static ch.epfl.alpano.gui.UserParameter.HEIGHT;
import static ch.epfl.alpano.gui.UserParameter.HORIZONTAL_FIELD_OF_VIEW;
import static ch.epfl.alpano.gui.UserParameter.MAX_DISTANCE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_ELEVATION;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LATITUDE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LONGITUDE;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.steganography.Helper;
import ch.epfl.alpano.steganography.Steganography;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public final class Alpano extends Application {
    public final static String version = "Alpano Version 1.0 START ";
    
    private final ContinuousElevationModel cem;
    private final List<Summit> listSummit;
    private final PanoramaParametersBean panoramaParamBean;
    private final PanoramaComputerBean panoramaComputerBean;
    private final PanoramaUserParameters initialParams;

    private final Map<UserParameter, TextField> userParametersTextFieldMap;
    private final ChoiceBox<Integer> userParametersChoiceBoxSupersampling;

    //parameters for the recalculate notice
    private static final int FONT_SIZE_RECALCULATE = 40;
    private static final double TRANSPARENCY_UPDATE_NOTICE = 0.9;

    //parameters for the grid
    private static final int PARAMS_GRID_PADDING = 5;
    private static final int PARAMAS_GRID_VERTICAL_SPACING = 3;
    private static final int PARAMS_GRID_HORIZONTAL_SPACING = 10;
    
    //parameters for  the progress bar.
    private static final double EPSILON_PROGRESS_BAR_VISIBILITY = 0.01;
    private static final double PROGRESS_BAR_WIDTH_SCREEN_WIDTH_DIVIDED_BY = 6;
    private static final double PROGRESS_BAR_HEIGHT_SCREEN_HEIGHT_DIVIDED_BY = 30;
    private static final double OPACITY_OF_PROGRESS_BAR_BACKGROUND = 1;
    
    private TextArea taPointerInfo;
    private GridPane paramsGrid;
    private ImageView panoView;
    private Pane labelsPane;
    private StackPane panoGroup;
    private Text updateText;
    private ScrollPane panoScrollPane;
    private StackPane updateNotice;
    private StackPane panoPane;
    private BorderPane root;
    private Scene scene;

    private StackPane calculationNotice;
    private ProgressBar calculationProgressBar;

    //***************** MENU *********
    private Menu menuPredefinedPanorama;
    private Menu menuOptions;
    private MenuBar menuBar;
    private MenuItem saveItem;
    private MenuItem fullScreenItem;
    
    private Menu choiceOfBrowerMap;
    private ToggleGroup tGroupMap;
    private RadioMenuItem googleItem;
    private RadioMenuItem openStreetMapsItem;
    
    private RadioMenuItem embedPropertyInPicture;
    private Menu saveOptionsMenu;
    
    
    
    static private final File HGT_FILE0 = new File("N45E006.hgt");
    static private final File HGT_FILE1 = new File("N45E007.hgt");
    static private final File HGT_FILE4 = new File("N45E008.hgt");
    static private final File HGT_FILE6 = new File("N45E009.hgt");
    static private final File HGT_FILE2 = new File("N46E006.hgt");
    static private final File HGT_FILE3 = new File("N46E007.hgt");
    static private final File HGT_FILE5 = new File("N46E008.hgt");
    static private final File HGT_FILE7 = new File("N46E009.hgt");
    
    //TODO use this in teh hgt import method to allow for creation of panorama all around switzerland.
    static private final File HGT_FILE8 = new File("N45E010.hgt");
    static private final File HGT_FILE9 = new File("N45E011.hgt");
    static private final File HGT_FILE10 = new File("N46E010.hgt");
    static private final File HGT_FILE11 = new File("N46E011.hgt");
    static private final File HGT_FILE12 = new File("N47E006.hgt");
    static private final File HGT_FILE13 = new File("N47E007.hgt");
    static private final File HGT_FILE14 = new File("N47E008.hgt");
    static private final File HGT_FILE15 = new File("N47E009.hgt");
    static private final File HGT_FILE16 = new File("N47E010.hgt");
    static private final File HGT_FILE17 = new File("N47E011.hgt");
    
    static private final File summitFile = new File("alps.txt");

    public Alpano() throws Exception{
        cem = loadHgtDEM();
        listSummit = GazetteerParser.readSummitsFrom(summitFile);
        initialParams = PredefinedPanoramas.ALP_JURA;
        panoramaParamBean = new PanoramaParametersBean(initialParams);
        panoramaComputerBean = new PanoramaComputerBean(cem, listSummit);
        userParametersTextFieldMap = new EnumMap<UserParameter, TextField>(UserParameter.class);
        userParametersChoiceBoxSupersampling = new ChoiceBox<>();

        calculationProgressBar = new ProgressBar();
        calculationNotice = new StackPane(calculationProgressBar);

        taPointerInfo = new TextArea();
        paramsGrid = new GridPane();
        panoView = new ImageView();
        labelsPane = new Pane();
        panoGroup = new StackPane(panoView, labelsPane);
        updateText = new Text();
        updateNotice = new StackPane(updateText);
        panoScrollPane = new ScrollPane(panoGroup);
        panoPane = new StackPane(panoScrollPane, calculationNotice, updateNotice);
        root = new BorderPane();
        scene = new Scene(root);
        //Underscore indicates the letter that will be used as a shortcut to acces the menue with alt + letter. 
        menuPredefinedPanorama = new Menu("_Predefined Panoramas");
        menuOptions = new Menu("_Options");

        menuBar = new MenuBar();
        tGroupMap = new ToggleGroup();
        saveItem = new MenuItem("Save");
        fullScreenItem = new MenuItem("Full Screen");
        choiceOfBrowerMap = new Menu("Choice of map website");
        googleItem = new RadioMenuItem("Google Maps");
        openStreetMapsItem = new RadioMenuItem("OpenStreetMaps");
        
        embedPropertyInPicture = new RadioMenuItem("Embed image properties");
        saveOptionsMenu = new Menu("Save Options");

    }

    @Override
    public void start(Stage primaryStage) {
        initParamsGrid();
        initPanoView();
        initLabelsPane();      
        initUpdateText();
        initUpdateNotice();
        initProgressBar();

        
        
        taPointerInfo.setPrefRowCount(2);
        taPointerInfo.setEditable(false);
        paramsGrid.add(taPointerInfo, 7, 0,1,3);

        initMenuBar(primaryStage);

        root.setBottom(paramsGrid);
        root.setTop(menuBar);
        root.setCenter(panoPane);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:alpano_icon.png"));
        primaryStage.setMaximized(true);

        primaryStage.setOnCloseRequest((e)-> System.exit(0));       //terminates all threads. Important because otherwise calculation threads persists until completion of calculation.
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void initMenuBar(Stage primaryStage) {
        menuPredefinedPanorama.setMnemonicParsing(true);    //enabeling shortcuts with alt.
        menuOptions.setMnemonicParsing(true);               //enabeling shortcuts with alt.
        addPredefinedPanoramaToMenu(menuPredefinedPanorama);
        addOptionsToMenu(menuOptions, primaryStage);

        menuBar.useSystemMenuBarProperty().set(true);
        menuBar.getMenus().addAll(menuPredefinedPanorama, menuOptions);
    }

    private void addOptionsToMenu(Menu menu, Stage primaryStage) {
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(saveMenuAction());

        fullScreenItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        fullScreenItem.setOnAction((e) -> {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("Press ESC to exit full screen view.");
        });

        googleItem.setToggleGroup(tGroupMap);
        openStreetMapsItem.setToggleGroup(tGroupMap);
        openStreetMapsItem.setSelected(true);       //Default startup setting
        
        choiceOfBrowerMap.getItems().addAll(openStreetMapsItem,googleItem);
        
//        saveOptionsMenu.getItems().add(embedPropertyInPicture);
        menu.getItems().addAll(saveItem, fullScreenItem,new SeparatorMenuItem(), choiceOfBrowerMap);
    }

    private void initProgressBar() {
        calculationNotice.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, OPACITY_OF_PROGRESS_BAR_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        BooleanProperty calcNoticeIsVisible = new SimpleBooleanProperty(false);
        calculationProgressBar.progressProperty().bind(panoramaComputerBean.getStateOfExecution());
        calcNoticeIsVisible.bind(panoramaComputerBean.getStateOfExecution().isNotEqualTo(1,EPSILON_PROGRESS_BAR_VISIBILITY));
        calculationNotice.visibleProperty().bind(calcNoticeIsVisible);
        calculationProgressBar.prefWidthProperty().bind(root.widthProperty().divide(PROGRESS_BAR_WIDTH_SCREEN_WIDTH_DIVIDED_BY));
        calculationProgressBar.prefHeightProperty().bind(root.heightProperty().divide(PROGRESS_BAR_HEIGHT_SCREEN_HEIGHT_DIVIDED_BY));
    }

    private void addPredefinedPanoramaToMenu(Menu menu) {
        MenuItem nisen = new MenuItem("Niesen");
        MenuItem alpJura = new MenuItem("Alp Jura");
        MenuItem montRacine = new MenuItem("Mont Racine");
        MenuItem finsterrahorn = new MenuItem("Finsterrarhorn");
        MenuItem tourDeSaubabelin = new MenuItem("Tour de Saubabelin");
        MenuItem plageDuPelican = new MenuItem("Plage du Pelican");

        //linking menu items to listener.
        nisen.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.NIESEN));
        alpJura.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.ALP_JURA));
        montRacine.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.MONT_RACINE));
        finsterrahorn.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.FINSTERRARHORN));
        tourDeSaubabelin.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.TOUR_DE_SAUBABELIN));
        plageDuPelican.setOnAction(predefinedPanoramaMenuAction(PredefinedPanoramas.PLAGE_DU_PELICAN));

        menu.getItems().addAll(nisen, alpJura, montRacine ,finsterrahorn, tourDeSaubabelin, plageDuPelican);
    }

    private EventHandler<ActionEvent> saveMenuAction(){
        return new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                try {    
                    JFileChooser fileChoser = new JFileChooser();
                    fileChoser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fileChoser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String saveLocation = fileChoser.getSelectedFile().toString()+".png";
                        ImageIO.write(SwingFXUtils.fromFXImage(panoramaComputerBean.getImage(), null),"png", new File(saveLocation));

//                       
//                        StringBuilder sb = new StringBuilder(version);
//                        sb.append("Latitude : ");
//                        sb.append(Math.toDegrees(panoramaComputerBean.getParameters().panoramaDisplayParameters().observerPosition().latitude()));
//                        int[][] image = Helper.fromBufferedImage(SwingFXUtils.fromFXImage(panoramaComputerBean.getImage(), null));
//                        image =  Steganography.embedText(image, sb.toString());
//                        ImagePainter fromArrayToBuffredImage = ImagePainter.argb(image);
//                        Image tempImage = PanoramaRenderer.renderPanorama(panoramaComputerBean.getPanorama(), fromArrayToBuffredImage);
//                        java.awt.image.BufferedImage tempImage = Helper.toBufferedImage(image);
//                        ImageIO.write(SwingFXUtils.fromFXImage(tempImage, null), "png", new File(saveLocation));
//                        ImageIO.write(tempImage, "png", new File(saveLocation));
                    }
                } catch ( Exception e) {
                    System.err.println(e.toString());
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Saving the picutre failed.");
                    alert.setContentText("Please contact the developement team for customer service");
                    alert.showAndWait();
                }
            }

        };
    }
    

    private EventHandler<ActionEvent> predefinedPanoramaMenuAction(PanoramaUserParameters panorama){
        return new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                userParametersTextFieldMap.get(OBSERVER_LATITUDE).
                setText(Math.toDegrees(panorama.panoramaDisplayParameters().observerPosition().latitude())+"");
                userParametersTextFieldMap.get(OBSERVER_LONGITUDE).
                setText(Math.toDegrees(panorama.panoramaDisplayParameters().observerPosition().longitude())+"");
                userParametersTextFieldMap.get(OBSERVER_ELEVATION).
                setText(panorama.panoramaDisplayParameters().observerElevation()+"");
                userParametersTextFieldMap.get(CENTER_AZIMUTH).
                setText(Math.toDegrees(panorama.panoramaDisplayParameters().centerAzimuth())+"");
                userParametersTextFieldMap.get(HORIZONTAL_FIELD_OF_VIEW).
                setText(Math.toDegrees(panorama.panoramaDisplayParameters().horizontalFieldOfView())+"");
                userParametersTextFieldMap.get(MAX_DISTANCE).
                setText(panorama.panoramaDisplayParameters().maxDistance()/1000d+"");
                userParametersTextFieldMap.get(WIDTH).
                setText(panorama.panoramaDisplayParameters().width()+"");
                userParametersTextFieldMap.get(HEIGHT).
                setText(panorama.panoramaDisplayParameters().height()+"");
                userParametersChoiceBoxSupersampling.setValue(panorama.superSamplingExponent());
            }
        };
    }
    
    

    private EventHandler<MouseEvent> mouseMovedTextUpdate(TextArea taPointerInfo){
        return new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                int superSam = panoramaComputerBean.getParameters().superSamplingExponent();
                Panorama p = panoramaComputerBean.getPanorama();
                int xCoord = (int)(e.getX()*Math.pow(2, superSam));
                int yCoord = (int)(e.getY()*Math.pow(2, superSam));
                Formatter formatter = new Formatter((Appendable) null,(Locale)null);

                double azimuth = p.parameters().azimuthForX(xCoord);    //in radiant
                double latitude = p.latitudeAt(xCoord, yCoord);         //in radiant
                double longitude = p.longitudeAt(xCoord, yCoord);       //in radiant
                taPointerInfo.setText(formatter.format(
                        "Position : %.4f%s %.4f%s"
                                + "%nDistance : %.1f km"
                                + "%nAltitude : %.0f m"
                                + "%nAzimuth : %.1f (%s)\t\tElévation : %.1f°",
                                Math.toDegrees(latitude), Math.signum(latitude) == 1.0 ? "°N" : "°S", Math.toDegrees(longitude), Math.signum(latitude) == 1.0 ? "°E" : "°W",
                                        (p.distanceAt(xCoord, yCoord)/1000f),
                                        p.elevationAt(xCoord, yCoord),
                                        Math.toDegrees(azimuth), toOctantString(azimuth, "N", "E", "S", "W"), Math.toDegrees(p.parameters().altitudeForY(yCoord))
                                        //adding some testing code for progress
                        ).toString());
                formatter.close();
            } 
        };
    }

    private EventHandler<MouseEvent> clickOnImageAction(){

        return new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                int superSam = panoramaComputerBean.getParameters().superSamplingExponent();
                Panorama p = panoramaComputerBean.getPanorama();
                int xCoord = (int)(e.getX()*Math.pow(2, superSam));
                int yCoord = (int)(e.getY()*Math.pow(2, superSam));
                String lambdaS = String.format((Locale)null, "%.4f", Math.toDegrees(p.longitudeAt(xCoord, yCoord)));
                String phiS = String.format((Locale)null, "%.4f", Math.toDegrees(p.latitudeAt(xCoord, yCoord)));
                String qy ="";
                String fg = "";
                if(openStreetMapsItem.isSelected()){
                    qy = "mlat=" + phiS + "&mlon=" + lambdaS;
                    fg = "#map=10/" +  phiS + "/" +   lambdaS;
                }
                if(googleItem.isSelected()){
                    qy = "/maps/place//@" + phiS + ",";
                    fg = lambdaS + ",11.5z"; 
                }
                try {
                    if(openStreetMapsItem.isSelected()){
                        URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
                        java.awt.Desktop.getDesktop().browse(osmURI);
                    }
                    if(googleItem.isSelected()){
                        URI osmURI = new URI("https://www.google.ch" +  qy + fg);
                        java.awt.Desktop.getDesktop().browse(osmURI);
                    }
                }catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException    e1) {
                    e1.printStackTrace();
                } 
            }   
        };
    }

    private static ContinuousElevationModel loadHgtDEM(){
        DiscreteElevationModel dem0 = new HgtDiscreteElevationModel(HGT_FILE0);
        DiscreteElevationModel dem1 = new HgtDiscreteElevationModel(HGT_FILE1);
        DiscreteElevationModel dem2 = new HgtDiscreteElevationModel(HGT_FILE2);
        DiscreteElevationModel dem3 = new HgtDiscreteElevationModel(HGT_FILE3);
        DiscreteElevationModel dem4 = new HgtDiscreteElevationModel(HGT_FILE4);
        DiscreteElevationModel dem5 = new HgtDiscreteElevationModel(HGT_FILE5);
        DiscreteElevationModel dem6 = new HgtDiscreteElevationModel(HGT_FILE6);
        DiscreteElevationModel dem7 = new HgtDiscreteElevationModel(HGT_FILE7);
        
        
        //****************FINISH ADDING THE UNION OF ALL TEH HGT FILES AVAILABLE TODO *****************
        DiscreteElevationModel dem8 = new HgtDiscreteElevationModel(HGT_FILE8);
        DiscreteElevationModel dem9 = new HgtDiscreteElevationModel(HGT_FILE9);
        DiscreteElevationModel dem10 = new HgtDiscreteElevationModel(HGT_FILE10);
        DiscreteElevationModel dem11 = new HgtDiscreteElevationModel(HGT_FILE11);
        DiscreteElevationModel dem12 = new HgtDiscreteElevationModel(HGT_FILE12);
        DiscreteElevationModel dem13 = new HgtDiscreteElevationModel(HGT_FILE13);
        DiscreteElevationModel dem14 = new HgtDiscreteElevationModel(HGT_FILE14);
        DiscreteElevationModel dem15 = new HgtDiscreteElevationModel(HGT_FILE15);
        DiscreteElevationModel dem16 = new HgtDiscreteElevationModel(HGT_FILE16);
        DiscreteElevationModel dem17 = new HgtDiscreteElevationModel(HGT_FILE17);
        
        return new ContinuousElevationModel((dem12.union(dem13).union(dem14).union(dem15).union(dem16).union(dem17)).union(((dem8.union(dem9)).union(dem10.union(dem11))).union((dem0.union(dem1).union((dem2).union(dem3))).union((dem4.union(dem5).union((dem6).union(dem7)))))));

//        return new ContinuousElevationModel((dem0.union(dem1).union((dem2).union(dem3))).union((dem4.union(dem5).union((dem6).union(dem7)))));
    }

    private void initUpdateNotice(){
        updateNotice.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, TRANSPARENCY_UPDATE_NOTICE), CornerRadii.EMPTY, Insets.EMPTY)));
        updateNotice.visibleProperty().bind( panoramaComputerBean.parametersProperty().isNotEqualTo(panoramaParamBean.parametersProperty()));
        updateNotice.setOnMouseClicked(e -> {
            panoramaComputerBean.setParameters(panoramaParamBean.parametersProperty().get());
        });
    }

    private void initUpdateText(){
        updateText.setText("Les paramètres du panorama ont changé.\n Cliquez ici pour mettre le dessin à jour.");
        updateText.setFont(new Font(FONT_SIZE_RECALCULATE));
        updateText.setTextAlignment(TextAlignment.CENTER);
    }

    private void initLabelsPane(){
        Bindings.bindContent(labelsPane.getChildren(), panoramaComputerBean.getLabels());
        labelsPane.prefWidthProperty().bind(panoramaParamBean.widthProperty());
        labelsPane.prefHeightProperty().bind(panoramaParamBean.heightProperty());
        labelsPane.setMouseTransparent(true);
    }

    private void initPanoView(){
        panoView.imageProperty().bind(panoramaComputerBean.imageProperty());
        panoView.fitWidthProperty().bind(panoramaParamBean.widthProperty());
        panoView.setPreserveRatio(true);
        panoView.setSmooth(true);

        //adding listener to the ImageView to show the information of the mouse.
        panoView.setOnMouseMoved(mouseMovedTextUpdate(taPointerInfo));  
        panoView.setOnMouseClicked(clickOnImageAction());
    }

    private void initParamsGrid(){
        //Set the padding and margins of each component of the grid.
        paramsGrid.setVgap(PARAMAS_GRID_VERTICAL_SPACING);
        paramsGrid.setHgap(PARAMS_GRID_HORIZONTAL_SPACING);
        paramsGrid.setPadding(new Insets(PARAMS_GRID_PADDING+2,PARAMS_GRID_PADDING,PARAMS_GRID_PADDING,PARAMS_GRID_PADDING));

        //creating the choice box for supersampling
        userParametersChoiceBoxSupersampling.getItems().addAll(0,1,2);
        StringConverter<Integer> formatteur = new LabeledListStringConverter("non", "2x", "4x");
        userParametersChoiceBoxSupersampling.setConverter(formatteur);
        userParametersChoiceBoxSupersampling.valueProperty().bindBidirectional(panoramaParamBean.superSamplingExponentProperty());

        userParametersTextFieldMap.put(OBSERVER_LATITUDE, createTextField(4, 7, panoramaParamBean.observerLatitudeProperty()));
        userParametersTextFieldMap.put(OBSERVER_LONGITUDE, createTextField(4, 7, panoramaParamBean.observerLongitudeProperty()));
        userParametersTextFieldMap.put(OBSERVER_ELEVATION, createTextField(0, 4, panoramaParamBean.observerElevationProperty()));
        userParametersTextFieldMap.put(CENTER_AZIMUTH, createTextField(0, 3, panoramaParamBean.centerAzimuthProperty()));
        userParametersTextFieldMap.put(HORIZONTAL_FIELD_OF_VIEW, createTextField(0, 3, panoramaParamBean.horizontalFieldOfViewProperty()));
        userParametersTextFieldMap.put(MAX_DISTANCE, createTextField(0, 3, panoramaParamBean.maxDistanceProperty()));
        userParametersTextFieldMap.put(WIDTH, createTextField(0, 4, panoramaParamBean.widthProperty()));
        userParametersTextFieldMap.put(HEIGHT, createTextField(0, 4, panoramaParamBean.heightProperty()));


        //Creating the paramsGrid and adding all the TextFileds
        paramsGrid.addRow(0, new Label("Latitude (°) : "), userParametersTextFieldMap.get(OBSERVER_LATITUDE),
                new Label("Longitude (°) : "), userParametersTextFieldMap.get(OBSERVER_LONGITUDE),
                new Label("Altitude (m) : "), userParametersTextFieldMap.get(OBSERVER_ELEVATION));
        paramsGrid.addRow(1, new Label("Azimut (°) : "), userParametersTextFieldMap.get(CENTER_AZIMUTH),
                new Label("Angle de vue (°) :"), userParametersTextFieldMap.get(HORIZONTAL_FIELD_OF_VIEW),
                new Label("Visibilité (km) : "), userParametersTextFieldMap.get(MAX_DISTANCE));
        paramsGrid.addRow(2, new Label("Largeur (px) : "), userParametersTextFieldMap.get(WIDTH),
                new Label("Hauteur (px) : "), userParametersTextFieldMap.get(HEIGHT),
                new Label("Suréchantillonnage : "),userParametersChoiceBoxSupersampling);
    }

    static private TextField createTextField(int posStringConverter, int collumCount, ObjectProperty<Integer> paramToBindTo){
        TextField field = new TextField();
        FixedPointStringConverter converter = new FixedPointStringConverter(posStringConverter);
        TextFormatter<Integer> formatter = new TextFormatter<>(converter);
        formatter.valueProperty().bindBidirectional(paramToBindTo);
        field.setTextFormatter(formatter);
        field.setAlignment(Pos.BASELINE_RIGHT);
        field.setPrefColumnCount(collumCount);
        return field;
    }
}