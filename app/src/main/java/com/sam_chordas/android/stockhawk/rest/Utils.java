package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private static String LOG_TAG = Utils.class.getSimpleName ();

    public static boolean showPercent = true;

    public static ArrayList quoteJsonToContentVals (String JSON) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<> ();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        Log.i (LOG_TAG, "GET FB: " + JSON);
        try {
            jsonObject = new JSONObject (JSON);
            if (jsonObject != null && jsonObject.length () != 0) {
                jsonObject = jsonObject.getJSONObject ("query");
                int count = Integer.parseInt (jsonObject.getString ("count"));
                if (count == 1) {
                    jsonObject = jsonObject.getJSONObject ("results").getJSONObject ("quote");
                    batchOperations.add (buildBatchOperation (jsonObject));
                } else {
                    resultsArray = jsonObject.getJSONObject ("results").getJSONArray ("quote");

                    if (resultsArray != null && resultsArray.length () != 0) {
                        for (int i = 0; i < resultsArray.length (); i++) {
                            jsonObject = resultsArray.getJSONObject (i);
                            batchOperations.add (buildBatchOperation (jsonObject));
                        }
                    }
                }
            }
        }
        catch (JSONException e) {
            Log.e (LOG_TAG, "String to JSON failed: " + e);
        }
     //   batchOperations.add (buildTestBatchOperation ());
        return batchOperations;
    }

    public static String truncateBidPrice (String bidPrice) {
        bidPrice = String.format ("%.2f", Float.parseFloat (bidPrice));
        return bidPrice;
    }

    public static String getCreated () {
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return sdf.format (new Date ());
    }

    public static String getCreatedTest () {
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return sdf.format (new Date (new Date ().getTime () - 1 * 24 * 60 * 60 * 1000));
    }

    public static String truncateChange (String change, boolean isPercentChange) {
        String weight = change.substring (0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring (change.length () - 1, change.length ());
            change = change.substring (0, change.length () - 1);
        }
        change = change.substring (1, change.length ());
        double round = (double) Math.round (Double.parseDouble (change) * 100) / 100;
        change = String.format ("%.2f", round);
        StringBuffer changeBuffer = new StringBuffer (change);
        changeBuffer.insert (0, weight);
        changeBuffer.append (ampersand);
        change = changeBuffer.toString ();
        return change;
    }

    public static ContentProviderOperation buildTestBatchOperation () {
//This method provides a fake entry for DB to test the chart
        String testString = "{\"symbol\":\"MSFT\",\"Ask\":\"52.00\",\"AverageDailyVolume\":\"40726800\",\"Bid\":\"51.85\",\"AskRealtime\":null,\"BidRealtime\":null,\"BookValue\":\"9.69\",\"Change_PercentChange\":\"-0.32 - -0.61%\",\"Change\":\"-0.32\",\"Commission\":null,\"Currency\":\"USD\",\"ChangeRealtime\":null,\"AfterHoursChangeRealtime\":null,\"DividendShare\":\"1.44\",\"LastTradeDate\":\"3\\/4\\/2016\",\"TradeDate\":null,\"EarningsShare\":\"1.41\",\"ErrorIndicationreturnedforsymbolchangedinvalid\":null,\"EPSEstimateCurrentYear\":\"2.76\",\"EPSEstimateNextYear\":\"3.06\",\"EPSEstimateNextQuarter\":\"0.67\",\"DaysLow\":\"51.71\",\"DaysHigh\":\"52.45\",\"YearLow\":\"39.72\",\"YearHigh\":\"56.85\",\"HoldingsGainPercent\":null,\"AnnualizedGain\":null,\"HoldingsGain\":null,\"HoldingsGainPercentRealtime\":null,\"HoldingsGainRealtime\":null,\"MoreInfo\":null,\"OrderBookRealtime\":null,\"MarketCapitalization\":\"411.52B\",\"MarketCapRealtime\":null,\"EBITDA\":\"30.46B\",\"ChangeFromYearLow\":\"12.31\",\"PercentChangeFromYearLow\":\"+30.99%\",\"LastTradeRealtimeWithTime\":null,\"ChangePercentRealtime\":null,\"ChangeFromYearHigh\":\"-4.82\",\"PercebtChangeFromYearHigh\":\"-8.48%\",\"LastTradeWithTime\":\"3:59pm - <b>52.03<\\/b>\",\"LastTradePriceOnly\":\"52.03\",\"HighLimit\":null,\"LowLimit\":null,\"DaysRange\":\"51.71 - 52.45\",\"DaysRangeRealtime\":null,\"FiftydayMovingAverage\":\"51.62\",\"TwoHundreddayMovingAverage\":\"50.40\",\"ChangeFromTwoHundreddayMovingAverage\":\"1.63\",\"PercentChangeFromTwoHundreddayMovingAverage\":\"+3.24%\",\"ChangeFromFiftydayMovingAverage\":\"0.41\",\"PercentChangeFromFiftydayMovingAverage\":\"+0.80%\",\"Name\":\"Microsoft Corporation\",\"Notes\":null,\"Open\":\"52.44\",\"PreviousClose\":\"52.35\",\"PricePaid\":null,\"ChangeinPercent\":\"-0.61%\",\"PriceSales\":\"4.70\",\"PriceBook\":\"5.40\",\"ExDividendDate\":\"2\\/16\\/2016\",\"PERatio\":\"36.82\",\"DividendPayDate\":\"3\\/10\\/2016\",\"PERatioRealtime\":null,\"PEGRatio\":\"1.99\",\"PriceEPSEstimateCurrentYear\":\"18.85\",\"PriceEPSEstimateNextYear\":\"17.00\",\"Symbol\":\"MSFT\",\"SharesOwned\":null,\"ShortRatio\":\"1.19\",\"LastTradeTime\":\"3:59pm\",\"TickerTrend\":null,\"OneyrTargetPrice\":\"58.56\",\"Volume\":\"33034150\",\"HoldingsValue\":null,\"HoldingsValueRealtime\":null,\"YearRange\":\"39.72 - 56.85\",\"DaysValueChange\":null,\"DaysValueChangeRealtime\":null,\"StockExchange\":\"NMS\",\"DividendYield\":\"2.74\",\"PercentChange\":\"-0.61%\"}";
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert (QuoteProvider.Quotes.CONTENT_URI);
        try {
            JSONObject jsonObject = new JSONObject (testString);
            String change = jsonObject.getString ("Change");
            builder.withValue (QuoteColumns.SYMBOL, jsonObject.getString ("symbol"));
            builder.withValue (QuoteColumns.CREATED, getCreatedTest ());
            builder.withValue (QuoteColumns.BIDPRICE, truncateBidPrice (jsonObject.getString ("Bid")));
            builder.withValue (QuoteColumns.PERCENT_CHANGE, truncateChange (StringUtils.isEmpty (jsonObject.getString ("ChangeinPercent")) ? "0" : jsonObject.getString ("ChangeinPercent"), true));
            builder.withValue (QuoteColumns.CHANGE, truncateChange (change, false));
            builder.withValue (QuoteColumns.ISCURRENT, 1);
            if (change.charAt (0) == '-') {
                builder.withValue (QuoteColumns.ISUP, 0);
            } else {
                builder.withValue (QuoteColumns.ISUP, 1);
            }

        }
        catch (JSONException e) {
            e.printStackTrace ();
        }
        return builder.build ();
    }

    public static ContentProviderOperation buildBatchOperation (JSONObject jsonObject) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert (QuoteProvider.Quotes.CONTENT_URI);
        try {
            String change = jsonObject.getString ("Change");
            builder.withValue (QuoteColumns.SYMBOL, jsonObject.getString ("symbol"));
            builder.withValue (QuoteColumns.CREATED, getCreated ());
            builder.withValue (QuoteColumns.BIDPRICE, truncateBidPrice (jsonObject.getString ("Bid")));
            builder.withValue (QuoteColumns.PERCENT_CHANGE, truncateChange (StringUtils.isEmpty (jsonObject.getString ("ChangeinPercent")) ? "0" : jsonObject.getString ("ChangeinPercent"), true));
            builder.withValue (QuoteColumns.CHANGE, truncateChange (change, false));
            builder.withValue (QuoteColumns.ISCURRENT, 1);
            if (change.charAt (0) == '-') {
                builder.withValue (QuoteColumns.ISUP, 0);
            } else {
                builder.withValue (QuoteColumns.ISUP, 1);
            }

        }
        catch (JSONException e) {
            e.printStackTrace ();
        }
        return builder.build ();
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo ();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
