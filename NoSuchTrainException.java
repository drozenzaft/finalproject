public class NoSuchTrainException extends RuntimeException {

    public NoSuchTrainException(){
        super();
    }

    public NoSuchTrainException(String message){
        super(message);
    }
}
