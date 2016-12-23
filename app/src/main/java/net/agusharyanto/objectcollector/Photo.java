package net.agusharyanto.objectcollector;

public class Photo {
	   public String fileseq = null;
	    public String shortfilename = null;
	    public String fullpathfile = null;
	    
	    public Photo(String pfileseq, String pshortfilename, String pfullpathfile){
	    	this.fileseq=pfileseq;
	    	this.shortfilename=pshortfilename;
	    	this.fullpathfile=pfullpathfile;
	    }
	    @Override
	    public String toString() {
	        StringBuffer sb = new StringBuffer();
	        sb.append("fileseq:" + fileseq + ", ");
	        sb.append("shortfilename:" + shortfilename + ", ");
	        sb.append("fullpatfile:" + fullpathfile + ", ");
	        
	        return sb.toString();
	    }
}
