package com.technologygroup.rayannoor.yoga.Services;

import android.util.Log;

import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Models.CoachCourseModel;
import com.technologygroup.rayannoor.yoga.Models.CoachEduModel;
import com.technologygroup.rayannoor.yoga.Models.CoachGymsModel;
import com.technologygroup.rayannoor.yoga.Models.CoachHonorModel;
import com.technologygroup.rayannoor.yoga.Models.CoachModel;
import com.technologygroup.rayannoor.yoga.Models.CoachResumeModel;
import com.technologygroup.rayannoor.yoga.Models.CommentModel;
import com.technologygroup.rayannoor.yoga.Models.CourseModel;
import com.technologygroup.rayannoor.yoga.Models.FAQmodel;
import com.technologygroup.rayannoor.yoga.Models.GalleryModel;
import com.technologygroup.rayannoor.yoga.Models.GymCoachesModel;
import com.technologygroup.rayannoor.yoga.Models.GymModel;
import com.technologygroup.rayannoor.yoga.Models.StateCityFieldModel;
import com.technologygroup.rayannoor.yoga.Models.TeachesModel;
import com.technologygroup.rayannoor.yoga.Models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by EHSAN on 3/1/2018.
 */

public class WebService {

    private String connectToServer(String address, String RequestMethod) {
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(RequestMethod);
            return inputStreamToString(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String connectToServerByJson(String address, String requestMethod, String JsonDATA) {

        String JsonResponse = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writter
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
// json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
//input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine);
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
//response data
            Log.i(TAG, JsonResponse);
            //send to post execute
            return JsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return null;

    }

    private String inputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String nextLine;
        try {
            while ((nextLine = reader.readLine()) != null) {
                stringBuilder.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    //android upload file to server
    public int uploadFile(boolean isInternetAvailable, String selectedFilePath, String fileName) {

        if (isInternetAvailable) {

            int serverResponseCode = 0;

            HttpURLConnection connection;
            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";


            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(selectedFilePath);


            String[] parts = selectedFilePath.split("/");
            //final String fileName = parts[parts.length - 1];

            selectedFilePath = "";

            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    selectedFilePath += fileName;
                } else {

                    selectedFilePath += parts[i];
                    selectedFilePath += "/";
                }
            }


            if (!selectedFile.isFile()) {
                //dialog.dismiss();

                // Toast.makeText(g, "فایل یافت نشد", Toast.LENGTH_LONG).show();

                return -1;
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(App.apiAddr + "upload/UploadFiles");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("uploaded_file", selectedFilePath);

                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    //writing bytes to data outputstream
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + selectedFilePath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                    Log.i(GifHeaderParser.TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
//                if (serverResponseCode == 200) {
//                    return 1;
//                }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return -2;

                    //Toast.makeText(getContext(), "مشکل در آپلود فایل", Toast.LENGTH_SHORT).show();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(), "مشکل در اتصال برای آپلود", Toast.LENGTH_SHORT).show();
                    return -3;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -4;
                    //Toast.makeText(getContext(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                }

                return serverResponseCode;
            }

        }
        return -5;
    }


    public UserModel userLogin(boolean isInternetAvailable, String userName, String pass) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "User/Login?Mobile=" + userName + "&Password=" + pass, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                UserModel model = new UserModel();

                if (response.equals("-1")) {

                    model.id = -1;

                } else {


                    try {

                        JSONObject Object = new JSONObject(response);

                        model.Email = Object.getString("Email");
                        model.Name = Object.getString("Name");
                        model.Password = Object.getString("Password");
                        model.lName = Object.getString("lName");
                        model.id = Object.getInt("id");
                        model.idCity = Object.getInt("idCity");
                        model.Type = Object.getInt("Type");
                        model.Mobile = Object.getString("Mobile");


                        return model;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;

        } else
            return null;
    }

    public UserModel userRegister(boolean isInternetAvailable, UserModel model) {

        if (isInternetAvailable) {

//            String req = "{\"idCity\":-1,\"Name\":\"" + model.Name + "\",\"lName\":\"" + model.lName + "\",\"Mobile\":\"" + model.Mobile + "\",\"Email\":\"" + model.Email + "\",\"Password\":\"" + model.Password + "\",\"Type\":3,\"isVisible\":true,\"lastUpdate\":1}";
//            String response = connectToServerByJson(App.apiAddr + "User/add", "POST", req);

            String response = connectToServer(App.apiAddr + "User/Register?name=" + model.Name + "&lname=" + model.lName + "&mobile=" + model.Mobile + "&email=" + model.Email + "&password=" + model.Password, "GET");
            Log.i("LOG", response + "");


            if (response != null) {

                UserModel modelNew = new UserModel();

                if (response.equals("-1")) {

                    modelNew.id = -1;

                } else {


                    try {

                        JSONObject Object = new JSONObject(response);

                        modelNew.Email = Object.getString("Email");
                        modelNew.Name = Object.getString("Name");
                        modelNew.Password = Object.getString("Password");
                        modelNew.lName = Object.getString("lName");
                        modelNew.id = Object.getInt("id");
                        modelNew.idCity = Object.getInt("idCity");
                        modelNew.Type = Object.getInt("Type");
                        modelNew.Mobile = Object.getString("Mobile");


                        return modelNew;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        } else
            return null;
    }


    public List<StateCityFieldModel> getStates(boolean isInternetAvailable) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "state/get", "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<StateCityFieldModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        StateCityFieldModel model = new StateCityFieldModel();

                        model.idState = Object.getInt("ID");
                        model.stateName = Object.getString("Name");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<StateCityFieldModel> getCiteies(boolean isInternetAvailable, int idState) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "City/GetByStateID/" + idState, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<StateCityFieldModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        StateCityFieldModel model = new StateCityFieldModel();

                        model.idCity = Object.getInt("ID");
                        model.cityName = Object.getString("Name");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<StateCityFieldModel> getFields(boolean isInternetAvailable) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "Field/get", "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<StateCityFieldModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        StateCityFieldModel model = new StateCityFieldModel();

                        model.idState = Object.getInt("ID");
                        model.stateName = Object.getString("Name");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }


    public CoachModel getCoachInfo(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "coach/select/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                CoachModel model = new CoachModel();

                try {

                    JSONArray Arrey = new JSONArray(response);

                    JSONObject Object = Arrey.getJSONObject(0);

                    model.Email = Object.getString("Email");
                    model.fName = Object.getString("fName");
                    model.Instagram = Object.getString("Instagram");
                    model.lName = Object.getString("lName");
                    model.Telegram = Object.getString("Telegram");
                    model.Img = Object.getString("Img");
                    model.id = Object.getInt("id");
                    model.idCity = Object.getInt("idCity");
                    model.idState = Object.getInt("idState");
                    model.idCurrentPlan = Object.getInt("idCurrentPlan");
                    model.like = Object.getInt("like");
                    model.lastUpdate = Object.getString("lastUpdate");
                    model.Mobile = Object.getString("Mobile");
                    model.natCode = Object.getString("natCode");
                    model.Rate = Object.getDouble("Rate");
                    model.City = Object.getString("City");
                    model.State = Object.getString("State");
                    model.Gender = Object.getBoolean("Gender");


                    return model;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CoachResumeModel> getCoachResume(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "WorkResume/get/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachResumeModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachResumeModel model = new CoachResumeModel();

                        model.id = Object.getInt("id");
                        model.endDate = Object.getString("endDate");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.idCoach = Object.getInt("idCoach");
                        model.Name = Object.getString("Name");
                        model.startDate = Object.getString("startDate");
                        model.Title = Object.getString("Title");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public String AddCoachResume(boolean isInternetAvailable, CoachResumeModel model) {

        if (isInternetAvailable) {

            String endDate;

            if (model.endDate.equals(""))
                endDate = "";
            else
                endDate = model.endDate.substring(0, 4);

//            String req = "{\"id\":-1,\"idCoach\":" + model.idCoach + ",\"Title\":\"" + model.Title + "\",\"startDate\":" + model.startDate.substring(0, 4) + ",\"endDate\":" + endDate + ",\"lastUpdate\":0}";
//            String response = connectToServerByJson(App.apiAddr + "Resume/add", "POST", req);
            String response = connectToServer(App.apiAddr + "WorkResume/Add?uid=" + model.idCoach + "&title=" + model.Title + "&startDate=" + model.startDate + "&endDate=" + endDate, "GET");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String editCoachResume(boolean isInternetAvailable, CoachResumeModel model) {

        if (isInternetAvailable) {

            String endDate;

            if (model.endDate.equals(""))
                endDate = "";
            else
                endDate = model.endDate.substring(0, 4);

//            String req = "{\"id\":" + model.id + ",\"idCoach\":" + model.idCoach + ",\"Title\":\"" + model.Title + "\",\"startDate\":" + model.startDate.substring(0, 4) + ",\"endDate\":" + endDate + ",\"lastUpdate\":0}";
//            String response = connectToServerByJson(App.apiAddr + "Resume/update", "POST", req);
            String response = connectToServer(App.apiAddr + "WorkResume/Edit?rid=" + model.id + "&title=" + model.Title + "&startDate=" + model.startDate + "&endDate=" + endDate, "GET");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String deleteCoachResume(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServerByJson(App.apiAddr + "WorkResume/Delete", "POST", id + "");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String editCoachBio(boolean isInternetAvailable, String bio, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "user/editbio?uid=" + id + "&bio=" + bio, "GET");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public List<CoachCourseModel> getCoachCourses(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "user/GetCourses/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachCourseModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachCourseModel model = new CoachCourseModel();

                        model.id = Object.getInt("id");
                        model.title = Object.getString("Title");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }








    // below codes are not edited


    public String editUserInfo(boolean isInternetAvailable, UserModel model) {

        if (isInternetAvailable) {

            String req = "{\"id\":" + model.id + ",\"idCity\":" + model.idCity + ",\"Name\":\"" + model.Name + "\",\"lName\":\"" + model.lName + "\",\"Mobile\":\"" + model.Mobile + "\",\"Email\":\"" + model.Email + "\",\"Password\":\"" + model.Password + "\",\"Type\":3,\"isVisible\":true,\"lastUpdate\":1}";
            String response = connectToServerByJson(App.apiAddr + "User/update", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }


    public String editCoachInfo(boolean isInternetAvailable, CoachModel model) {

        if (isInternetAvailable) {

            String req = "{\"id\":" + model.id + ",\"idCity\":" + -1 + ",\"idCurrentPlan\":" + -1 + ",\"fName\":\"" + model.fName + "\",\"lName\":\"" + model.lName + "\",\"natCode\":" + model.natCode + ",\"like\":" + -1 + ",\"Rate\":" + -1 + ",\"rateCount\":" + -1 + ",\"Mobile\":" + model.Mobile + ",\"Telegram\": \"" + model.Telegram + "\",\"Email\": \"" + model.Email + "\",\"Instagram\": \"" + model.Instagram + "\",\"lastUpdate\":0,\"image\":\"" + model.Img + "\"}";
            String response = connectToServerByJson(App.apiAddr + "coach/update", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public List<CoachEduModel> getCoachEdu(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "EducationalRecord/select/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachEduModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachEduModel model = new CoachEduModel();

                        model.id = Object.getInt("id");
                        model.Date = Object.getString("Date");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.idCoach = Object.getInt("idCoach");
                        model.Name = Object.getString("Name");
                        model.CoachName = Object.getString("CoachName");
                        model.Img = Object.getString("Img");
                        model.gettingPlace = Object.getString("gettingPlace");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CoachHonorModel> getCoachHonor(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "honor/SelectCoachHonorrs/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachHonorModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachHonorModel model = new CoachHonorModel();

                        model.id = Object.getInt("id");
                        model.Date = Object.getString("Date");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.idRow = Object.getInt("idRow");
                        model.Name = Object.getString("Name");
                        model.Title = Object.getString("Title");
                        model.Img = Object.getString("Img");
                        model.Des = Object.getString("Des");
                        model.isGym = Object.getBoolean("isGym");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<TeachesModel> getTeaches(boolean isInternetAvailable, int id, boolean isGym) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "Training/select?isgym=" + isGym + "&id=" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<TeachesModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        TeachesModel model = new TeachesModel();

                        model.id = Object.getInt("id");
                        model.idRow = Object.getInt("idRow");
                        model.Date = Object.getString("Date");
                        model.isGym = Object.getBoolean("isGym");
                        model.Title = Object.getString("Title");
                        model.Body = Object.getString("Body");
                        model.Images = Object.getString("Images");
                        model.Name = Object.getString("Name");
                        model.ImagePersonal = Object.getString("ImagePersonal");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CoachGymsModel> getCoachGyms(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "GymCoaches/selectGyms/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachGymsModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachGymsModel model = new CoachGymsModel();

                        model.id = Object.getInt("id");
                        model.Img = Object.getString("Img");
                        model.Name = Object.getString("Name");
                        model.like = Object.getInt("like");
                        model.Rate = Object.getDouble("Rate");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CommentModel> getComments(boolean isInternetAvailable, boolean isGym, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "comment/select?isgym=" + isGym + "&idrow=" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CommentModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CommentModel model = new CommentModel();

                        model.id = Object.getInt("id");
                        model.name = Object.getString("name");
                        model.body = Object.getString("body");
                        model.date = Object.getInt("date");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }


    public String AddCoachEdu(boolean isInternetAvailable, CoachEduModel model) {

        if (isInternetAvailable) {

            String req = "{\"idCoach\":" + model.idCoach + ",\"Name\":\"" + model.Name + "\",\"image\":\"" + model.Img + "\",\"gettingPlace\":\"" + model.gettingPlace + "\",\"lastUpdate\":0,\"date\":" + model.Date.substring(0, 4) + "}";
            String response = connectToServerByJson(App.apiAddr + "EducationalRecord/add", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String AddCoachHonor(boolean isInternetAvailable, CoachHonorModel model) {

        if (isInternetAvailable) {

            String req = "{\"Date\":" + model.Date.substring(0, 4) + ",\"Des\":\"" + model.Des + "\",\"id\":" + -1 + ",\"idRow\":" + model.idRow + ",\"isGym\":false,\"lastUpdate\":0,\"Title\":\"" + model.Title + "\",\"Image\":\"" + model.Img + "\",\"Name\":\"" + model.Name + "\"}";
            String response = connectToServerByJson(App.apiAddr + "honor/add", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String AddComment(boolean isInternetAvailable, CommentModel model) {

        if (isInternetAvailable) {


            String req = "{\"id\":-1,\"idRow\":" + model.idRow + ",\"idUser\":" + model.idUser + ",\"Body\":\"" + model.body + "\",\"Date\":" + model.date + ",\"isGym\":" + model.isGym + ",\"isActive\":true,\"lastUpdets\":1,\"isRead\":false}";
            String response = connectToServerByJson(App.apiAddr + "comment/add", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String AddTeaches(boolean isInternetAvailable, TeachesModel model) {

        if (isInternetAvailable) {

            String req = "{\"idRow\":" + model.idRow + ",\"Title\":\"" + model.Title + "\",\"Body\":\"" + model.Body + "\",\"Images\":\"" + model.Images + "\",\"Date\":" + model.Date + ",\"isVisible\":true,\"isGym\":" + model.isGym + ",\"lastUpdate\":1}";
            String response = connectToServerByJson(App.apiAddr + "Training/add", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }


    public String editCoachEdu(boolean isInternetAvailable, CoachEduModel model) {

        if (isInternetAvailable) {

            String req = "{\"id\":" + model.id + ",\"idCoach\":" + model.idCoach + ",\"Name\":\"" + model.Name + "\",\"image\":\"" + model.Img + "\",\"gettingPlace\":\"" + model.gettingPlace + "\",\"lastUpdate\":0,\"date\":" + model.Date.substring(0, 4) + "}";
            String response = connectToServerByJson(App.apiAddr + "EducationalRecord/update", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String editCoachHonor(boolean isInternetAvailable, CoachHonorModel model) {

        if (isInternetAvailable) {

            String req = "{\"Date\":" + model.Date.substring(0, 4) + ",\"Des\":\"" + model.Des + "\",\"id\":" + model.id + ",\"idRow\":" + model.idRow + ",\"isGym\":false,\"lastUpdate\":0,\"Title\":\"" + model.Title + "\",\"Image\":\"" + model.Img + "\",\"Name\":\"" + model.Name + "\"}";
            String response = connectToServerByJson(App.apiAddr + "honor/update", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String editTeaches(boolean isInternetAvailable, TeachesModel model) {

        if (isInternetAvailable) {

            String req = " {\"id\":" + model.id + ",\"idRow\":" + model.idRow + ",\"Title\":\"" + model.Title + "\",\"Body\":\"" + model.Body + "\",\"Images\":\"" + model.Images + "\",\"Date\":" + model.Date + ",\"isVisible\":true,\"isGym\":false,\"lastUpdate\":1}";
            String response = connectToServerByJson(App.apiAddr + "Training/update", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }


    public String deleteCoachEdu(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServerByJson(App.apiAddr + "EducationalRecord/delete", "POST", id + "");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String deleteCoachHhonor(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServerByJson(App.apiAddr + "honor/delete", "POST", id + "");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String deleteTeaches(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServerByJson(App.apiAddr + "Training/delete", "POST", id + "");
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String deleteImgDetails(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "images/delete?id=" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                return response;

            }
            return null;
        } else
            return null;
    }


    public String postLike(boolean isInternetAvailable, int idCoachOrGym, int idUser, String type) {

        if (isInternetAvailable) {

            String req = "{\"id\":" + idCoachOrGym + ",\"idUser\":" + idUser + "}";
            String response = connectToServerByJson(App.apiAddr + type + "/like", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String postRate(boolean isInternetAvailable, int idCoachOrGym, int idUser, String type, float rate) {

        if (isInternetAvailable) {

            String req = "{\"id\":" + idCoachOrGym + ",\"idUser\":" + idUser + ",\"newRate\":" + rate + "}";
            String response = connectToServerByJson(App.apiAddr + type + "/Rate", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public String postPlanId(boolean isInternetAvailable, int idPlan, int idUser) {

        if (isInternetAvailable) {

            String req = "{\"idUser\":" + idUser + ",\"idPlan\":" + idPlan + "}";
            String response = connectToServerByJson(App.apiAddr + "payment/pay", "POST", req);
            Log.i("LOG", response + "");

            return response;
        } else
            return null;
    }

    public List<CoachModel> getCoachesByField(boolean isInternetAvailable, int idField) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "Coach/GetByField/" + idField, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachModel> list = new ArrayList<>();

                try {

                    JSONArray Array = new JSONArray(response);

                    for (int i = 0; i < Array.length(); i++) {

                        JSONObject Object = Array.getJSONObject(i);
                        CoachModel model = new CoachModel();
                        model.Email = Object.getString("Email");
                        model.id = Object.getInt("idUser");
                        model.fName = Object.getString("fName");
                        model.idCity = Object.getInt("idCity");
                        model.idState = Object.getInt("idState");
                        model.idCurrentPlan = Object.getInt("idCurrentPlan");
                        model.Instagram = Object.getString("Instagram");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.like = Object.getInt("like");
                        model.lName = Object.getString("lName");
                        model.Mobile = Object.getString("Mobile");
                        model.natCode = Object.getString("natCode");
                        model.Rate = Object.getDouble("Rate");
                        model.Telegram = Object.getString("Telegram");
                        model.Img = Object.getString("Img");
                        model.City = Object.getString("City");
                        model.State = Object.getString("State");
                        model.Gender = Object.getBoolean("Gender");
                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        } else
            return null;
    }

    public List<GymModel> getGymsByField(boolean isInternetAvailable) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "gym/selectlist", "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<GymModel> list = new ArrayList<>();

                try {

                    JSONArray Array = new JSONArray(response);

                    for (int i = 0; i < Array.length(); i++) {

                        JSONObject Object = Array.getJSONObject(i);
                        GymModel model = new GymModel();

                        model.idCity = Object.getInt("idCity");
                        model.Des = Object.getString("Des");
                        model.idCurrentSMSPlan = Object.getInt("idCurrentSMSPlan");
                        model.Lat = Object.getDouble("Lat");
                        model.Lon = Object.getDouble("Lon");
                        model.Name = Object.getString("Name");
                        model.notifCount = Object.getInt("notifCount");
                        model.Tell = Object.getString("Tell");
                        model.workTime = Object.getString("workTime");
                        model.Email = Object.getString("Email");
                        model.fname = Object.getString("fname");
                        model.id = Object.getInt("id");
                        model.idCurrentPlan = Object.getInt("idCurrentPlan");
                        model.Instagram = Object.getString("Instagram");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.like = Object.getInt("like");
                        model.lName = Object.getString("LName");
                        model.Address = Object.getString("Address");
                        model.Rate = Object.getDouble("Rate");
                        model.Telegram = Object.getString("Telegram");
                        model.City = Object.getString("City");
                        model.State = Object.getString("State");
                        model.Img = Object.getString("Img");

                        list.add(model);


                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        } else
            return null;
    }

    public GymModel getGymInfo(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "gym/select/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                GymModel model = new GymModel();

                try {

                    JSONArray Arrey = new JSONArray(response);

                    JSONObject Object = Arrey.getJSONObject(0);

                    model.Email = Object.getString("Email");
                    model.fname = Object.getString("fname");
                    model.Instagram = Object.getString("Instagram");
                    model.lName = Object.getString("lName");
                    model.Telegram = Object.getString("Telegram");
                    model.Img = Object.getString("Img");
                    model.id = Object.getInt("id");
                    model.idCity = Object.getInt("idCity");
                    model.notifCount = Object.getInt("notifCount");
                    model.idCurrentPlan = Object.getInt("idCurrentPlan");
                    model.like = Object.getInt("like");
                    model.lastUpdate = Object.getString("lastUpdate");
                    model.Tell = Object.getString("Tell");
                    model.workTime = Object.getString("workTime");
                    model.Rate = Object.getDouble("Rate");
                    model.City = Object.getString("City");
                    model.State = Object.getString("State");
                    model.Des = Object.getString("Des");
                    model.idCurrentSMSPlan = Object.getInt("idCurrentSMSPlan");
                    model.Lat = Object.getDouble("Lat");
                    model.Lon = Object.getDouble("Lon");
                    model.Name = Object.getString("Name");
                    model.Address = Object.getString("Address");


                    return model;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CoachHonorModel> getGymHonor(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "honor/SelectGymHonorrs/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CoachHonorModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CoachHonorModel model = new CoachHonorModel();

                        model.id = Object.getInt("id");
                        model.Date = Object.getString("Date");
                        model.lastUpdate = Object.getString("lastUpdate");
                        model.idRow = Object.getInt("idRow");
                        model.Name = Object.getString("Name");
                        model.Title = Object.getString("Title");
                        model.Img = Object.getString("Img");
                        model.Des = Object.getString("Des");
                        model.isGym = Object.getBoolean("isGym");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<GalleryModel> getGymGallery(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "gym/gymGallery/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<GalleryModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        GalleryModel model = new GalleryModel();

                        model.id = Object.getInt("id");
                        model.img = Object.getString("img");
//                        model.Date = Object.getString("Date");
//                        model.lastUpdate = Object.getString("lastUpdate");
//                        model.idRow = Object.getInt("idRow");
//                        model.Name = Object.getString("Name");
//                        model.Title = Object.getString("Title");
//                        model.Img = Object.getString("Img");
//                        model.Des = Object.getString("Des");
//                        model.isGym = Object.getBoolean("isGym");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<GymCoachesModel> getGymCoaches(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "GymCoaches/gymCoach/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<GymCoachesModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        GymCoachesModel model = new GymCoachesModel();

                        model.idUser = Object.getInt("idUser");
                        model.Img = Object.getString("img");
                        model.fName = Object.getString("fName");
                        model.lName = Object.getString("lName");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public List<CourseModel> getGymCourses(boolean isInternetAvailable, int id) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "gym/gymTerms/" + id, "GET");
            Log.i("LOG", response + "");

            if (response != null) {

                List<CourseModel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        CourseModel model = new CourseModel();

                        model.idTerm = Object.getInt("idTerm");
                        model.startDate = Object.getString("startDate");
                        model.endDate = Object.getString("endDate");
                        model.coachName = Object.getString("coachName");
                        model.Title = Object.getString("Title");
                        model.Times = Object.getString("Times");
                        model.Days = Object.getString("Days");

                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }

    public String getAboutUs(boolean isInternetAvailable, String key) {

        String result="";

        if (isInternetAvailable) {


            String response = connectToServer(App.apiAddr + "kesho/getbyKey?key="+key, "GET");


            if (response != null) {

                try
                {
                    JSONObject Object = new JSONObject(response);
                    result = Object.getString("KeshoValue");

                    return result;
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            return result;
        } else
            return null;
    }

    public List<FAQmodel> getFAQ(boolean isInternetAvailable) {

        if (isInternetAvailable) {

            String response = connectToServer(App.apiAddr + "FAQ/Get", "GET");

            if (response != null) {

                List<FAQmodel> list = new ArrayList<>();

                try {

                    JSONArray Arrey = new JSONArray(response);
                    for (int i = 0; i < Arrey.length(); i++) {
                        JSONObject Object = Arrey.getJSONObject(i);
                        FAQmodel model = new FAQmodel();

                        model.question = Object.getString("Question");
                        model.answer = Object.getString("Answer");


                        list.add(model);

                    }
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        } else
            return null;
    }


}