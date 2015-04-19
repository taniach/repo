package teammates.common.datatransfer;

import java.util.Arrays;
import java.util.Map;


import teammates.common.util.Sanitizer;


public class FeedbackMcqResponseDetails extends FeedbackResponseDetails {
    private String answer;
    private boolean isOther;
    private String otherFieldContent; //content of other field if "other" is selected as the answer
    
    public FeedbackMcqResponseDetails() {
        super(FeedbackQuestionType.MCQ);
        answer = "";
        isOther = false;
        otherFieldContent = "";
    }
    
    @Override
    public void extractResponseDetails(FeedbackQuestionType questionType,
            FeedbackQuestionDetails questionDetails, String[] answer) {
        // TODO: check and set isOther accordingly when it is implemented.
        //isOther = HttpRequestHelper.getValueFromParamMap(requestParameters, Const.ParamsNames.FEEDBACK_QUESTION_MCQOTHEROPTION) != null;;
        System.out.println("questiontype = "+questionType);
        System.out.println("questiondetails = "+questionDetails.getQuestionTypeChoiceOption());
        System.out.println("questiondetails = "+questionDetails.getQuestionTypeDisplayName());
        System.out.println("ans[] = "+Arrays.toString(answer));
       
        if(isOther){
            this.answer = "Other";
            this.otherFieldContent = answer[0];
        } else {
            this.answer = answer[0];
            this.otherFieldContent = "";
        }
        System.out.println("isother = "+isOther);
        System.out.println("answer = "+this.answer);
        System.out.println("otherfieldcontent = "+this.otherFieldContent);
    }

    @Override
    public String getAnswerString() {
        if(isOther){
            return otherFieldContent;
        } else {
            return answer;
        }
    }

    @Override
    public String getAnswerHtml(FeedbackQuestionDetails questionDetails) {
        return Sanitizer.sanitizeForHtml(getAnswerString());
    }

    @Override
    public String getAnswerCsv(FeedbackQuestionDetails questionDetails) {
        return Sanitizer.sanitizeForCsv(getAnswerString());
    }

    public Boolean isOtherOptionAnswer() {
        return isOther;
    }
}
