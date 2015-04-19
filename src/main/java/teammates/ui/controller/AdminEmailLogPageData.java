package teammates.ui.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import teammates.common.datatransfer.AccountAttributes;
import teammates.common.util.Const;
import teammates.common.util.EmailLogEntry;
import teammates.common.util.TimeHelper;

public class AdminEmailLogPageData extends PageData {

    public String offset;
    public String filterQuery;
    public String queryMessage;
    public List<EmailLogEntry> logs;
    public List<String> versions;
    
    public boolean shouldShowAll;
    public String statusForAjax;
    private QueryParameters q;
    
    public AdminEmailLogPageData(AccountAttributes account) {
        super(account);
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * Creates a QueryParameters object used for filtering
     */
    public void generateQueryParameters(String query){
        query = query.toLowerCase();
        
        try{
            q = parseQuery(query);
        } catch (Exception e){
            this.queryMessage = "Error with the query: " + e.getMessage();
        }
    }
    
    
    /**
     * Converts the query string into a QueryParameters object
     * 
     */
    private QueryParameters parseQuery(String query) throws Exception{
        QueryParameters q = new QueryParameters();
        versions = new ArrayList<String>();
        
        if(query == null || query.equals("")){
            return q;
        }
        
        query = query.replaceAll(" and ", "|");
        query = query.replaceAll(", ", ",");
        query = query.replaceAll(": ", ":");
        String[] tokens = query.split("\\|", -1); 
       
        System.out.print(tokens.length);
        
        for(int i = 0; i < tokens.length; i++){           
            String[] pair = tokens[i].split(":", -1);
            
            if(pair.length != 2){
                throw new Exception("Invalid format");
            }
            
            String[] values = pair[1].split(",", -1);
            String label = pair[0];
            
            if (label.equals("version")) {
                //version is specified in com.google.appengine.api.log.LogQuery,
                //it does not belong to the internal class "QueryParameters"
                //so need to store here for future use
                for (int j = 0; j < values.length; j++) {
                    versions.add(values[j].replace(".", "-"));
                }
                
            } else {
                q.add(label, values);
            }
        }
        
        return q;
    }
    
    /**
     * Performs the actual filtering, based on QueryParameters
     * returns false if the logEntry fails the filtering process
     */
    public boolean shouldShowLog(EmailLogEntry logEntry){
        
        if(q == null){
            if (this.queryMessage == null){
                this.queryMessage = "Error parsing the query. QueryParameters not created.";
            }
            return true;
        }
        
        //Filter based on what is in the query
        if(q.isToDateInQuery){
            if(logEntry.getTime() > q.toDateValue){
                return false;
            }
        }
        if(q.isFromDateInQuery){
            if(logEntry.getTime() < q.fromDateValue){
                return false;
            }
        }
        if(q.isReceiverInQuery){
            
            for (String keyString : q.receiverValues){
                if(!logEntry.getReceiver().toLowerCase().contains(keyString.toLowerCase())){
                    return false;
                }
            }
            logEntry.highlightKeyStringInMessageInfoHtml(q.receiverValues, "receiver");
        }
        if(q.isSubjectInQuery){
    
            for (String keyString : q.subjectValues){
                if(!logEntry.getSubject().toLowerCase().contains(keyString.toLowerCase())){
                    return false;
                }
            }
            logEntry.highlightKeyStringInMessageInfoHtml(q.subjectValues, "subject");
        }
        if(q.isInfoInQuery){
            
            for (String keyString : q.infoValues){
                if(!logEntry.getContent().toLowerCase().contains(keyString.toLowerCase())){
                    return false;
                }
            }
            logEntry.highlightKeyStringInMessageInfoHtml(q.infoValues, "content");
        }
        
        return true;
    }
    
    
    /**
     * QueryParameters inner class. Used only within this servlet, to hold the query data once it is parsed
     * The boolean variables determine if the specific label was within the query
     * The XXValue variables hold the data linked to the label in the query
     */
    private class QueryParameters{        
        public boolean isToDateInQuery;
        public long toDateValue;
        
        public boolean isFromDateInQuery;
        public long fromDateValue;
        
        public boolean isReceiverInQuery;
        public String[] receiverValues;
        
        public boolean isSubjectInQuery;
        public String[] subjectValues;
        
        public boolean isInfoInQuery;
        public String[] infoValues;
        
        public QueryParameters(){
            isToDateInQuery = false;
            isFromDateInQuery = false;
            isReceiverInQuery = false;
            isSubjectInQuery = false;
            isInfoInQuery = false;
        }
        
        /**
         * add a label and values in
         */
        public void add(String label, String[] values) throws Exception{
            if(label.equals("after")){
                isFromDateInQuery = true;                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
                Date d = sdf.parse(values[0] + " 0:00");                          
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal = TimeHelper.convertToUserTimeZone(cal, - Const.SystemParams.ADMIN_TIMZE_ZONE_DOUBLE);
                fromDateValue = cal.getTime().getTime();
                
            } else if (label.equals("before")){
                isToDateInQuery = true;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
                Date d = sdf.parse(values[0] + " 23:59");  
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal = TimeHelper.convertToUserTimeZone(cal, - Const.SystemParams.ADMIN_TIMZE_ZONE_DOUBLE);
                toDateValue = cal.getTime().getTime();          
            } else if (label.equals("receiver")){
                isReceiverInQuery = true;
                receiverValues = values;
            } else if (label.equals("subject")){
                isSubjectInQuery = true;
                subjectValues = values;
            } else if (label.equals("info")){
                isInfoInQuery = true;
                infoValues = values;
            } else {
                throw new Exception("Invalid label");
            }
        }
    }

    
    
}
