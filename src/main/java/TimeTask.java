import java.util.TimerTask;

public class TimeTask extends TimerTask {
    private FirstServlet servlet;
    public TimeTask(FirstServlet servlet){
        this.servlet = servlet;
    }
    @Override
    public void run() {
        servlet.request();
    }
}
