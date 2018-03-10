package com.example.johanaanesen.imt3673_lab02;

import java.util.List;

/**
 * Created by Johan Aanesen on 3/10/2018.
 */

public interface Task {
    public void onSuccess(List<RssFeedModel> res);
    public void onError(Exception err);
}
