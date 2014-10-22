package ch.hsr.inte.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;


public class Entry  implements Serializable {


	private static final long serialVersionUID = 6253510300688876168L;
	private String link;
	private String title;
	private String author;
	private int votes;
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public long getId() {
		return date.getTime();
	}
	
	private LinkedList<Comment> comments = new LinkedList<Comment>();

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LinkedList<Comment> getComments() {
		return comments;
	}

	public void setComments(LinkedList<Comment> comments) {
		this.comments = comments;
	}
	
	

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
}
