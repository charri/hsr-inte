package ch.hsr.inte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.hsr.inte.jsf.RelativeDateFormat;
import ch.hsr.inte.model.Comment;
import ch.hsr.inte.model.Entry;

@ManagedBean(name = "entryBean", eager = true)
@SessionScoped
public class EntryBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String serFileName = "/tmp/entries.ser";
	private File serFile;
	
	@SuppressWarnings("unchecked")
	public EntryBean() {
		serFile = new File(serFileName);
		if (serFile.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(serFileName);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				entries = (LinkedList<Entry>) objectIn.readObject();
				objectIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private LinkedList<Entry> entries = new LinkedList<Entry>();
	public LinkedList<Entry> getEntries() {
		return entries;
	}
	public void setEntries(LinkedList<Entry> entries) {
		this.entries = entries;
	}
	
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	private String link;
	private String commentMessage;
	
	private boolean showSubmit;
	
	@ManagedProperty(value="#{userBean}")
    private UserBean userBean; // +setter
	
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public void addEntry() throws IOException {
		Entry entry = new Entry();
		entry.setTitle(getTitle());
		entry.setLink(getLink());
		entry.setAuthor(userBean.getAlias());
		entry.setDate(new Date());
		entries.addFirst(entry);
		
		save();
		
		setTitle("");
		setLink("");
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}
	
	public Entry getCurrent() {
		if(!FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("id")) return null;
		
		long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
		
		for (Entry entry : entries) {
			if(entry.getId() == id)
				return entry;
		}
		return null;
	}
	
	public long getCurrentId() {
		Entry current = getCurrent();
		if(current == null) return -1;
		return current.getId();
	}
	
	private void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream(serFile);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(entries);
			objectOut.flush();
			objectOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRelativeDate(Date date) {
		RelativeDateFormat rdf = new RelativeDateFormat(date);
		rdf.setSecondFormatter(new DecimalFormat("0"));
		rdf.setDaySuffix("days ");
        rdf.setHourSuffix("h ");
        rdf.setMinuteSuffix("min ");
        rdf.setSecondSuffix("s ");
		return rdf.format(new Date());
	}
	
	public void upVote(Entry entry) {
		entry.setVotes(entry.getVotes()+ 1);
	
		sortEntries();
		
		save();
	}
	
	public void sortEntries() {
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry o1, Entry o2) {
				return o2.getVotes() - o1.getVotes();
			}
		});
	}
	
	public void sortComments(Entry entry) {
		Collections.sort(entry.getComments(), new Comparator<Comment>() {
			@Override
			public int compare(Comment o1, Comment o2) {
				return o2.getVotes() - o1.getVotes();
			}
		});
	}
	
	public void upVote(Comment comment) {
		comment.setVotes(comment.getVotes() + 1);
		sortComments(getCurrent());
		save();
	}
	
	public void downVote(Entry entry) {
		entry.setVotes(entry.getVotes()- 1);
		sortEntries();
		save();	
	}
	
	public void downVote(Comment comment) {
		comment.setVotes(comment.getVotes() - 1);
		sortComments(getCurrent());
		save();
	}
	public String getCommentMessage() {
		return commentMessage;
	}
	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}
	
	public void addComment(Entry current) throws IOException {

		
		Comment c = new Comment();
		c.setAuthor(getUserBean().getAlias());
		c.setDate(new Date());
		c.setMessage(getCommentMessage());
		c.setVotes(0);
		
		current.getComments().addFirst(c);
		
		setCommentMessage("");
		
		save();
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("detail.xhtml?id="+ current.getId());
	}
	public boolean getShowSubmit() {
		return showSubmit;
	}
	public void setShowSubmit(boolean showSubmit) {
		this.showSubmit = showSubmit;
	}
}
