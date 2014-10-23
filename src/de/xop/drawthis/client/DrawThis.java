package de.xop.drawthis.client;

import de.xop.drawthis.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Cursor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DrawThis implements EntryPoint {

	int thisX = 0;
	int thisY = 0;
	int lastX = 0;
	int lastY = 0;
	boolean paint = false;
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		final Canvas drawCanvas = Canvas.createIfSupported();
		//FlexTable roomList = new FlexTable();
		final Button sendButton = new Button("Send");
		DataGrid<String> roomGrid = new DataGrid<String>();
		
		String[] test = {"Room 1","Room 2","Hans's Room","Party","Pr0n"};
		
		Column<String, String> columnRooms = new Column<String, String>(new TextCell()) {

			@Override
			public String getValue(String object) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};	
		
		Column<String, String> columnPeople = new Column<String, String>(new TextCell()) {

			@Override
			public String getValue(String object) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};	
		
		roomGrid.addColumn(columnRooms, "Rooms");
		roomGrid.addColumn(columnPeople, "People");
		roomGrid.setColumnWidth(1, "70px");
		roomGrid.setWidth("300px");
		roomGrid.setHeight("300px");
		
		//final TextBox nameField = new TextBox();
		//nameField.setText("GWT User");
		//final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		//roomList.addStyleName("roomList");
		roomGrid.setStyleName("roomGrid");
		drawCanvas.setStyleName("drawCanvas");
		drawCanvas.setPixelSize(720,540);
		drawCanvas.setWidth("720");
		drawCanvas.setHeight("540");
		drawCanvas.setSize("720", "540");
		
		drawCanvas.setCoordinateSpaceWidth(720);
		drawCanvas.setCoordinateSpaceHeight(540);
		
		final Context2d context = drawCanvas.getContext2d();

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("canvasContainer").add(drawCanvas);
		//RootPanel.get("roomListContainer").add(roomList);
		RootPanel.get("roomGridContainer").add(roomGrid);
		RootPanel.get("sendButtonContainer").add(sendButton);
		
		/*roomList.setWidth("200px");
		roomList.setCellSpacing(5);
	    roomList.setCellPadding(3);
		
		roomList.setText(0, 0, "Room 1");
	    roomList.setText(0, 1, "1/6");
	    roomList.setText(1, 0, "Room 2");
	    roomList.setText(1, 1, "3/6");*/

		// Focus the cursor on the name field when the app loads
		//nameField.setFocus(true);
		//nameField.selectAll();

		// Create the popup dialog box
		
		/*final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});*/

		// Create a handler for the sendButton and nameField
		class CanvasHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOverHandler, MouseOutHandler{

			private Timer elapsedTimer = new Timer () {
				public void run() {
				}
			};

			@Override
			public void onMouseUp(MouseUpEvent event) {
				// TODO Auto-generated method stub
				paint = false;
			}

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				event.preventDefault();
				int mouseX = event.getX();
				int mouseY = event.getY();
				
				paint = true;
				
				thisX = mouseX;
				thisY = mouseY;
				lastX = mouseX;
				lastY = mouseY;
				redraw();

				drawCanvas.setFocus(false);
			}

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				// TODO Auto-generated method stub
				if(paint){

					int mouseX = event.getX();
					int mouseY = event.getY();
					thisX = mouseX;
					thisY = mouseY;
					redraw();
					drawCanvas.setFocus(false);
				}
			}
			
			private void redraw(){
				context.setStrokeStyle("#000000");
				context.setLineJoin("round");
				context.setLineWidth(5.0);
						
				context.beginPath();
				context.moveTo(lastX, lastY);
			    context.lineTo(thisX, thisY);
			    context.closePath();
				
			    context.stroke();
				lastX = thisX;
				lastY = thisY;
			}

			@Override
			public void onMouseOver(MouseOverEvent event) {
				// TODO Auto-generated method stub
				if(paint){
					int mouseX = event.getX();
					int mouseY = event.getY();

					lastX = mouseX;
					lastY = mouseY;
				}
			}

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				if(paint){
					int mouseX = event.getX();
					int mouseY = event.getY();
	
					thisX = mouseX;
					thisY = mouseY;
					redraw();
				}
			}
			
		}
		
//		class MyHandler implements ClickHandler, KeyUpHandler {
//			/**
//			 * Fired when the user clicks on the sendButton.
//			 */
//			public void onClick(ClickEvent event) {
//				sendNameToServer();
//			}
//
//			/**
//			 * Fired when the user types in the nameField.
//			 */
//			public void onKeyUp(KeyUpEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					sendNameToServer();
//				}
//			}
//
//			/**
//			 * Send the name from the nameField to the server and wait for a response.
//			 */
//			private void sendNameToServer() {
//				// First, we validate the input.
//				errorLabel.setText("");
//				String textToServer = nameField.getText();
//				if (!FieldVerifier.isValidName(textToServer)) {
//					errorLabel.setText("Please enter at least four characters");
//					return;
//				}
//
//				// Then, we send the input to the server.
//				sendButton.setEnabled(false);
//				textToServerLabel.setText(textToServer);
//				serverResponseLabel.setText("");
//				greetingService.greetServer(textToServer,
//						new AsyncCallback<String>() {
//							public void onFailure(Throwable caught) {
//								// Show the RPC error message to the user
//								dialogBox
//										.setText("Remote Procedure Call - Failure");
//								serverResponseLabel
//										.addStyleName("serverResponseLabelError");
//								serverResponseLabel.setHTML(SERVER_ERROR);
//								dialogBox.center();
//								closeButton.setFocus(true);
//							}
//
//							public void onSuccess(String result) {
//								dialogBox.setText("Remote Procedure Call");
//								serverResponseLabel
//										.removeStyleName("serverResponseLabelError");
//								serverResponseLabel.setHTML(result);
//								dialogBox.center();
//								closeButton.setFocus(true);
//							}
//						});
//			}
//		}

		// Add a handler to send the name to the server
	//	MyHandler handler = new MyHandler();
	//	sendButton.addClickHandler(handler);
	//	nameField.addKeyUpHandler(handler);
		CanvasHandler canvasHandler = new CanvasHandler();
		drawCanvas.addMouseDownHandler(canvasHandler);
		drawCanvas.addMouseUpHandler(canvasHandler);
		drawCanvas.addMouseMoveHandler(canvasHandler);
		drawCanvas.addMouseOverHandler(canvasHandler);
		drawCanvas.addMouseOutHandler(canvasHandler);
		
		ClickHandler button = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get("canvasContainer").remove(drawCanvas);
			}
		};

		sendButton.addClickHandler(button);
		
	}
}
