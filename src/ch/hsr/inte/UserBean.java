package ch.hsr.inte;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ch.hsr.inte.model.User;

@ManagedBean(name = "userBean", eager = true)
@SessionScoped
public class UserBean implements Serializable {

	private List<User> users = new ArrayList<User>();
	
	private String serFileName = "/tmp/users.ser";
	private File serFile;
	
	@SuppressWarnings("unchecked")
	public UserBean() {
		serFile = new File(serFileName);
		if (serFile.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(serFileName);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				users = (List<User>) objectIn.readObject();
				objectIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<User> getUsers() {
		return users;
	}
	private static final long serialVersionUID = 1L;
	private String alias;
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAlias() {
		return alias;
	}
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	private boolean isValid = false;
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	private boolean showSubmit;
	
	
	public String getValidation() {
		return getIsPostback() && !isValid ? "has-error" : "";
	}
	
	public boolean getIsPostback() {
	    return FacesContext.getCurrentInstance().isPostback();
	}
	
	private User find(String alias, String password) {
		
		for(User user : users) {
			if(user.getAlias().equals(alias) && (password == null || user.getPassword().equals(password))) {
				return user;
			}
		}
		
		return null;
	}
	
	public void login() throws IOException {
		User user = find(getAlias(), getPassword());
		if(user == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uhh ohh!", "Email or password wrong."));
			isValid = false;
			return;
		}
		isValid = true;
		
		setAlias(user.getAlias());
		
		Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap(); 
		
		if( map.containsKey("id") && !"-1".equals(map.get("id"))) {
		
			FacesContext.getCurrentInstance().getExternalContext().redirect("detail.xhtml?id=" + map.get("id"));
		} else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
		}
	}
	
	public void register() throws IOException {
		
		if(find(getAlias(), null) != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User already exists!", "A user already exists with that nickname."));
			return;
		}
		
		User user = new User();
		user.setAlias(getAlias());
		user.setPassword(getPassword());
		
		users.add(user);
		
		isValid = true;
		
		save();
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}
	
	private void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream(serFile);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(users);
			objectOut.flush();
			objectOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout() throws IOException {
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}

	public boolean getShowSubmit() {
		return showSubmit;
	}

	public void setShowSubmit(boolean showSubmit) {
		this.showSubmit = showSubmit;
	}
	
	public void toggleSubmit(ActionEvent event) {
		setShowSubmit(!getShowSubmit());
	}
}
