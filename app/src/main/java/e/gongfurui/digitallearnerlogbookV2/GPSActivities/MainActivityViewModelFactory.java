package e.gongfurui.digitallearnerlogbookV2.GPSActivities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

import e.gongfurui.digitallearnerlogbookV2.Helpers.GoogleMapHelper;
import e.gongfurui.digitallearnerlogbookV2.Utils.AppRxSchedulers;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final AppRxSchedulers appRxSchedulers;
    private final LocationRequest locationRequest;
    private final FusedLocationProviderClient locationProviderClient;
    private final GoogleMapHelper googleMapHelper;

    public MainActivityViewModelFactory(AppRxSchedulers appRxSchedulers, LocationRequest locationRequest, FusedLocationProviderClient locationProviderClient, GoogleMapHelper googleMapHelper) {
        this.appRxSchedulers = appRxSchedulers;
        this.locationRequest = locationRequest;
        this.locationProviderClient = locationProviderClient;
        this.googleMapHelper = googleMapHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(appRxSchedulers,locationRequest,locationProviderClient,googleMapHelper);
    }
}
