package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.shared.serialized.UserResultSer;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Friendsfootball implements EntryPoint {

	private final FriendsfootballServiceAsync footballService = GWT
			.create(FriendsfootballService.class);

	private final HLayout topLayoutRight = new HLayout();
	private final HLayout topLayoutLeftBottom = new HLayout();
	private final HLayout middleLayout = new HLayout();

	private String userKey = "";
	private String groupId = "";
	
	public void onModuleLoad() {
		
        RootPanel p = RootPanel.get("loadingWrapper");
        if (p != null) RootPanel.getBodyElement().removeChild(p.getElement());

        RootPanel.get().clear();
	    RootPanel.get().add(getViewPanel());
	}
	
	public Canvas getViewPanel() {
		
		VLayout mainLayout = new VLayout();
		mainLayout.setTitle(ClientConstants.TITLE);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
	
		HLayout topLayout = new HLayout();
		topLayout.setWidth("100%");
		topLayout.setHeight("66px");
		topLayout.setStyleName("top"); 
					
		VLayout topLayoutLeft = new VLayout();
		topLayoutLeft.setHeight("100%");
		topLayoutLeft.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				
		HLayout topLayoutLeftTop = new HLayout();
		topLayoutLeftTop.setHeight("50%");
		topLayoutLeftTop.setAlign(Alignment.CENTER);

		Label titleLabel = new Label(ClientConstants.TITLE + " " + ClientConstants.VERSION);   
		topLayoutLeftTop.addMember(titleLabel);

		topLayoutLeftBottom.setHeight("50%");
		
		topLayoutLeft.addMember(topLayoutLeftTop);
		topLayoutLeft.addMember(topLayoutLeftBottom);
		
		topLayoutRight.setWidth("360px");
		topLayoutRight.setHeight("100%");
		topLayoutRight.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				
		topLayout.addMember(topLayoutLeft);
		topLayout.addMember(topLayoutRight);		

		middleLayout.setDefaultLayoutAlign(VerticalAlignment.TOP);	
		middleLayout.setWidth("100%");
		middleLayout.setHeight("75%");
		middleLayout.setStyleName("context"); 
		
		HLayout bottomLayout = new HLayout();
		bottomLayout.setWidth("100%");
		bottomLayout.setHeight("30px");

		HLayout bottomLayoutLeft = new HLayout();
	
		bottomLayoutLeft.setHeight("100%");
		bottomLayoutLeft.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				
		DisplayRequest.getStatusLabel().setWidth100();
		bottomLayoutLeft.addMember(DisplayRequest.getStatusLabel());
		
		HLayout bottomLayoutRight = new HLayout();
		bottomLayoutRight.setWidth("180px");
		bottomLayoutRight.setHeight("100%");
				
		DisplayRequest.getProgressBar().setVertical(false);  		  
		bottomLayoutRight.addMember(DisplayRequest.getProgressBar());	

		bottomLayout.addMember(bottomLayoutLeft);
		bottomLayout.addMember(bottomLayoutRight);		
		
		mainLayout.addMember(topLayout);		
		mainLayout.addMember(middleLayout);
		mainLayout.addMember(bottomLayout);
		  	
		mainLayout.draw ();
				
		String loggedUserId = "";
		if (Cookies.getCookie(ClientConstants.COOKIE_USER) != null) loggedUserId = Cookies.getCookie(ClientConstants.COOKIE_USER);
		if (loggedUserId.equals(""))
			topLayoutRight.addMember(getLogin());
		else 
			getUser(loggedUserId,null,null);			
		
		new Timer() {  
            public void run() {  
             	if (DisplayRequest.getProgressBarValue() >= 0) { 
             		DisplayRequest.setProgressBarValue(DisplayRequest.getProgressBarValue() + 10);  
                	if (DisplayRequest.getProgressBarValue() > 100) DisplayRequest.setProgressBarValue(ClientConstants.PROGRESS_START);  
                	DisplayRequest.getProgressBar().setPercentDone(DisplayRequest.getProgressBarValue()); 	
				}	 	
           		schedule(ClientConstants.PROGRESS_SCHEDULE);           		
            }  
        }.schedule(ClientConstants.PROGRESS_SCHEDULE);  

        return mainLayout;
	}
	
	private HLayout getLogin () {
		topLayoutRight.removeMembers(topLayoutRight.getMembers());
        DynamicForm form = new DynamicForm(); 
        form.setPadding(2);  
        form.setLayoutAlign(VerticalAlignment.CENTER);  
                             
        final TextItem userIdItem = new TextItem();
        userIdItem.setTitle(ClientLabels.LOGIN_USER);
        userIdItem.setLength(15);
        userIdItem.setRequired(true);
        
        final PasswordItem passwordItem = new PasswordItem();  
        passwordItem.setTitle(ClientLabels.LOGIN_PASSWORD);  
        passwordItem.setLength(15);
        passwordItem.setRequired(true);
        
        final IButton loginButtonItem = new IButton(ClientLabels.LOGIN_BUTTON);     

        final CheckboxItem passwordCheckboxItem = new CheckboxItem();   
        passwordCheckboxItem.setTitle(ClientLabels.LOGIN_NEW_SENDPASSWORD);   
        
        loginButtonItem.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	if ((userIdItem.getValue() != null) && (passwordItem.getValue() != null)) {       
            		if (passwordCheckboxItem.getValue() == null) getUser(userIdItem.getValue().toString(),passwordItem.getValue().toString(),null);
            		else getUser(userIdItem.getValue().toString(),passwordItem.getValue().toString(),passwordCheckboxItem.getValue().toString());
            	}
            	
            	if ((userIdItem.getValue() != null) && (passwordItem.getValue() == null)) {
            		if (passwordCheckboxItem.getValue() != null)  {
            			DisplayRequest.getStatusLabel();
    	    			footballService.sendPassword(userIdItem.getValue().toString(),new AsyncCallback<Object>() {
    		    			public void onFailure(Throwable caught) {
    		    				DisplayRequest.serverError(); 	 	
    		    			}
    		    			public void onSuccess(Object result) {		        			
    		    				DisplayRequest.serverResponse();
    		    			}
    		    		});
            		}            			            		
            	}            	
             }  
        });  

        passwordItem.addChangedHandler(new ChangedHandler() {  
  			public void onChanged(ChangedEvent event) {
  				if (passwordItem.getValue() != null) passwordCheckboxItem.setTitle(ClientLabels.LOGIN_NEW_PASSWORD);   
  				else passwordCheckboxItem.setTitle(ClientLabels.LOGIN_NEW_SENDPASSWORD);
  				passwordCheckboxItem.redraw();
			}
        });  
        
        form.setFields(userIdItem,passwordItem,passwordCheckboxItem);
        HLayout hLayout = new HLayout();
    	hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
        hLayout.addMember(form);
        hLayout.addMember(loginButtonItem);       
        return hLayout;
	}

	
	private void getUser (final String userId, String password,final String passwordSetting) {
		DisplayRequest.processStart();
	
		footballService.user(userId, password, Navigator.getUserAgent().toString(), new AsyncCallback<UserResultSer>() {
			public void onFailure(Throwable caught) {
				DisplayRequest.serverError(); 	 	
			}

			public void onSuccess(UserResultSer userResultSer) {
				DisplayRequest.serverResponse(); 	 	
				if (userResultSer.getError() != null) {
					SC.warn(userResultSer.getError());
				}
				else  {
					
					Date now = new Date();
				    Date expire = new Date(now.getTime() - (ClientConstants.COOKIE_EXPIRE * 86400000));
					Cookies.setCookie(ClientConstants.COOKIE_USER, userId, expire);
					
					userKey = userResultSer.getKey();
					groupId = userResultSer.getId();
					if (groupId.contains(ClientConstants.GROUP_DELIMITER)) groupId = groupId.substring(0,groupId.lastIndexOf(ClientConstants.GROUP_DELIMITER));

					boolean query = false;
					if ((userResultSer.getRight() != null) && (userResultSer.getRight().length() >0))  {						
						if (userResultSer.getRight().charAt(0) == 'Y') {
							query = true;					
							Label emptyLabel1 = new Label("&nbsp;&nbsp");   

							IButton basicButtonItem = new IButton(ClientLabels.MENU_BASIC);             
				        	basicButtonItem.addClickHandler(new ClickHandler() {  
				        		public void onClick(ClickEvent event) {
				        			middleLayout.removeMembers(middleLayout.getMembers());
				        			middleLayout.addMember(new Maintenance().getLayout());
				        		}  
				        	});  	
				        	
				        	topLayoutLeftBottom.addMember(emptyLabel1);	
				        	topLayoutLeftBottom.addMember(basicButtonItem);				        	
				        	
						}  
					}   

					if ((userResultSer.getParticipantright() != null) && (userResultSer.getParticipantright().length() >0))  {						
						if (userResultSer.getParticipantright().charAt(0) == 'Y') {
							query = true;						
							Label emptyLabel2 = new Label("&nbsp;&nbsp");   

							IButton teamButtonItem = new IButton(ClientLabels.MENU_TEAM);             
				        	teamButtonItem.addClickHandler(new ClickHandler() {  
				        		public void onClick(ClickEvent event) {
				        			middleLayout.removeMembers(middleLayout.getMembers());
				        			middleLayout.addMember(new Team(userKey).getLayout());
				        		}  
				        	});  	
				        	
				        	topLayoutLeftBottom.addMember(emptyLabel2);	
				        	topLayoutLeftBottom.addMember(teamButtonItem);				        	
				        	
						}  
					}   										

					
					if (query) {
						
						Label emptyLabel3 = new Label("&nbsp;&nbsp");   

						IButton queryButtonItem = new IButton(ClientLabels.MENU_QUERY);             
			        	queryButtonItem.addClickHandler(new ClickHandler() {  
			        		public void onClick(ClickEvent event) {
			        			middleLayout.removeMembers(middleLayout.getMembers());
			        			middleLayout.addMember(new Query(userKey,groupId).getLayout());
			        		}  
			        	});  				

			        	topLayoutLeftBottom.addMember(emptyLabel3);	
			        	topLayoutLeftBottom.addMember(queryButtonItem);				        							
					}
					
					if ((userResultSer.getRight() != null) && (userResultSer.getRight().length() >0))  {						
						if (userResultSer.getRight().charAt(0) == 'Y') {	
							Label emptyLabel4 = new Label("&nbsp;&nbsp");   

							IButton historyButtonItem = new IButton(ClientLabels.MENU_HISTORY);             
				        	historyButtonItem.addClickHandler(new ClickHandler() {  
				        		public void onClick(ClickEvent event) {
				        			middleLayout.removeMembers(middleLayout.getMembers());
				        			middleLayout.addMember(new History().getLayout());
				        		}  
				        	});  	
				        	
				        	topLayoutLeftBottom.addMember(emptyLabel4);	
				        	topLayoutLeftBottom.addMember(historyButtonItem);	
				        	
							Label emptyLabel5 = new Label("&nbsp;&nbsp");   

							IButton listButtonItem = new IButton(ClientLabels.MENU_LIST);             
				        	listButtonItem.addClickHandler(new ClickHandler() {  
				        		public void onClick(ClickEvent event) {
				        			middleLayout.removeMembers(middleLayout.getMembers());
				        			middleLayout.addMember(new List1().getLayout());
				        		}  
				        	});  	
				        	
				        	topLayoutLeftBottom.addMember(emptyLabel5);	
				        	topLayoutLeftBottom.addMember(listButtonItem);				        					        					        	
				        	
						}  
					}   
					
					if (passwordSetting == null) topLayoutRight.addMember(getLogout(userResultSer.getName()));
					else topLayoutRight.addMember(getPassword(userResultSer.getName()));
					
					middleLayout.removeMembers(middleLayout.getMembers());		
					middleLayout.addMember(new Query(userKey,groupId).getLayout());
					
				}
			}
		});  		
	}
	
	private HLayout getPassword(final String name) {
		
		topLayoutRight.removeMembers(topLayoutRight.getMembers());
				
		final DynamicForm form = new DynamicForm(); 
	    form.setPadding(5);  
	    form.setLayoutAlign(VerticalAlignment.CENTER);
	    
        final PasswordItem passwordItem = new PasswordItem();   
        passwordItem.setName("password");
        passwordItem.setTitle(ClientLabels.PASSWORDSETTING_NEW_PASSWORD);
        passwordItem.setRequired(true);   
        passwordItem.setLength(20);  
  
        PasswordItem passwordItem2 = new PasswordItem();   
        passwordItem2.setName("password2");   
        passwordItem2.setTitle(ClientLabels.PASSWORDSETTING_PASWORD_AGAIN);   
        passwordItem2.setRequired(true);   
        passwordItem2.setLength(20);   
  
        MatchesFieldValidator matchesValidator = new MatchesFieldValidator();   
        matchesValidator.setOtherField("password");   
        matchesValidator.setErrorMessage(ClientLabels.PASSWORDSETTING_MATCH_ERROR);
        
    	CustomValidator passwordValidator = new CustomValidator() {
			@Override
			protected boolean condition(Object value) {
				if (value == null) return false;		
				String password = value.toString();
				if (password.equals(ClientConstants.INIT_PASSWORD)) return false; 
				return true;				
			}
		};
		
		passwordValidator.setErrorMessage(ClientLabels.PASSWORDSETTING_INIT_ERROR); 		
        passwordItem2.setValidators(passwordValidator,matchesValidator);   
  	  
	    form.setFields(passwordItem,passwordItem2);			
	    IButton changeButtonItem = new IButton(ClientLabels.PASSWORDSETTING_CHANGE);
	    
	    topLayoutRight.addMember(form);
	    topLayoutRight.addMember(changeButtonItem);
	    
	    changeButtonItem.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		DisplayRequest.getStatusLabel();
	    		if (form.validate(false)) {	    
	    			footballService.password(userKey,passwordItem.getValue().toString(),new AsyncCallback<Object>() {
		    			public void onFailure(Throwable caught) {
		    				DisplayRequest.serverError(); 	 	
		    			}
		    			public void onSuccess(Object result) {		        			
		    				DisplayRequest.serverResponse();
	    					topLayoutRight.addMember(getLogout(name));
		    			}
		    		});
	    		 }
    		 
	    	 }	
        });  
	 	    
        HLayout hLayout = new HLayout();
    	hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
        hLayout.addMember(form);
        hLayout.addMember(changeButtonItem);       
        return hLayout;
	}

	private HLayout getLogout(String name) {
		topLayoutRight.removeMembers(topLayoutRight.getMembers());
		Label nameLabel = new Label(name);   
	    IButton logoutButtonItem = new IButton(ClientLabels.LOGOUT_BUTTON);     
	    
	    logoutButtonItem.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    			    		
	    		Cookies.removeCookie(ClientConstants.COOKIE_USER);
	    		topLayoutRight.addMember(getLogin());	    		
	    		topLayoutLeftBottom.removeMembers(topLayoutLeftBottom.getMembers());;	    			    		
	    		middleLayout.removeMembers(middleLayout.getMembers());    				
	    	 }	
        });   	    
        
        HLayout hLayout = new HLayout();
    	hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
        hLayout.addMember(nameLabel);
        hLayout.addMember(logoutButtonItem);       
        return hLayout;
	}

}
