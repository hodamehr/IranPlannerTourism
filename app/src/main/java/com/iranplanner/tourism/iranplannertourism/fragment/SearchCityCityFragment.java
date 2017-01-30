package com.iranplanner.tourism.iranplannertourism.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.iranplanner.tourism.iranplannertourism.R;
import com.iranplanner.tourism.iranplannertourism.autoComplet.MyFilterableAdapterCity;
import com.iranplanner.tourism.iranplannertourism.autoComplet.readJsonCity;
import com.iranplanner.tourism.iranplannertourism.entity.City;
import com.iranplanner.tourism.iranplannertourism.entity.ResultItinerary;
import com.iranplanner.tourism.iranplannertourism.entity.ResultItineraryList;
import com.iranplanner.tourism.iranplannertourism.server.getJsonInterface;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import standard.StandardFragment;


public class SearchCityCityFragment extends StandardFragment implements Callback<ResultItineraryList> {
    List<City> cities;
    String cityFrom;
    String cityEnd;
    AutoCompleteTextView fromCity_city, endCity_city;
    boolean checkfragment = false;
    ProgressBar waitingForData;
    RelativeLayout SearchHolderForWatiting;

    public static SearchCityCityFragment newInstance() {
        SearchCityCityFragment fragment = new SearchCityCityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_city_city, container, false);
        fromCity_city = (AutoCompleteTextView) view.findViewById(R.id.fromCity_city);
        endCity_city = (AutoCompleteTextView) view.findViewById(R.id.endCity_city);
        Button searchOk_city = (Button) view.findViewById(R.id.searchOk_city);
//         waitingForData = (ProgressBar) view.findViewById(R.id.waitingForData);
//        SearchHolderForWatiting = (RelativeLayout) view.findViewById(R.id.SearchHolderForWatiting);
        final List<City> temp1 = autoComplete(fromCity_city);
        final List<City> temp2 = autoComplete(endCity_city);

        searchOk_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //// TODO: 17/01/2017 inja code asli hast
                cityFrom = returnCityId(fromCity_city, temp1);
                cityEnd = returnCityId(endCity_city, temp2);
                if (cityFrom != null && cityEnd != null) {
                    getItinerary(cityFrom, "0", false);
//                    SearchHolderForWatiting.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getActivity(), "please correct name of city ", Toast.LENGTH_SHORT).show();
                }
                Log.d("search ok clicked", "true");
            }
        });
        return view;
    }

    public List<City> autoComplete(AutoCompleteTextView city) {
        readJsonCity readJsonCity = new readJsonCity();
        cities = readJsonCity.getListOfCity(getContext());
        MyFilterableAdapterCity adapter = new MyFilterableAdapterCity(getContext(), android.R.layout.simple_list_item_1, cities);
        city.setAdapter(adapter);
        return cities;
    }

    public String returnCityId(AutoCompleteTextView textView, List<City> cityList) {
        String cityId = null;
        for (City c : cityList) {
            Log.e("city", c.getCityName());
            if (c.getCityName().equals(textView.getText().toString())) {
                cityId = c.getCityId();
            }
        }
        return cityId;
    }


    public void getItinerary(String cityId, String offset, boolean checkfragment) {
        this.checkfragment = checkfragment;
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.parsdid.com/iranplanner/app/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getJsonInterface getJsonInterface = retrofit.create(getJsonInterface.class);
        Call<ResultItineraryList> call = getJsonInterface.getItinerarys("list", "fa", cityId, "", offset);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResultItineraryList> call, Response<ResultItineraryList> response) {
//        Log.e("get result from server", response.body().toString());
        if (response.body() != null) {
            ResultItineraryList jsonResponse = response.body();
            List<ResultItinerary> data = jsonResponse.getResultItinerary();
            ItineraryListFragment itineraryListFragment = new ItineraryListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("resuliItineraryList", (Serializable) data);
            bundle.putString("fromWhere", "fromCityToCity");
            bundle.putString("nextOffset", response.body().getStatistics().getOffsetNext().toString());
            itineraryListFragment.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.SearchHolder, itineraryListFragment);
            ft.addToBackStack(null);
            ft.commit();
            checkfragment = true;
//            SearchHolderForWatiting.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onFailure(Call<ResultItineraryList> call, Throwable t) {
        Toast.makeText(getContext(), "Internet kooooooooooooooooooo?", Toast.LENGTH_LONG).show();
        Log.e(" error from server", "error");
    }

}
