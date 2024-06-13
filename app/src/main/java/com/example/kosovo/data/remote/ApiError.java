package com.example.kosovo.data.remote;


import android.util.MalformedJsonException;

import com.esafirm.imagepicker.features.ImagePicker;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class ApiError {
    public static class ErrorMessage{
        public String message;
        public int status;

        public ErrorMessage(String message, int status) {
            this.message = message;
            this.status = status;
        }
    }

    public static ErrorMessage getErrorFromException(Exception e){
        return new ErrorMessage(e.getMessage(),e.hashCode());
    }
    public static ErrorMessage getErrorFromThrowable(Throwable t){
        if (t instanceof retrofit2.HttpException){
            return  new ErrorMessage(t.getMessage(),((retrofit2.HttpException) t).code());
        }else if (t instanceof SocketTimeoutException){
            return new ErrorMessage("Time out", 0);

        }else if (t instanceof IOException){
            if (t instanceof MalformedJsonException){
                return new ErrorMessage("MalformedJsonException from server", 0);
            }else if (t instanceof ConnectException){
                return new ErrorMessage(t.getMessage()+ "Your xammp is not working or \n you have different ip address", 0);
            }else {
                return new ErrorMessage("No internet connection", 0);
            }
        }else {
            return new ErrorMessage("Unknown error", 0);
        }
    }
}
