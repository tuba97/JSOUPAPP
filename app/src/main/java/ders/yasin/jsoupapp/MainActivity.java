package ders.yasin.jsoupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAnns,btnNews,btnLinks,btnOffices;
    TextView tvTitle;
    ListView lvJsoup;
    String URL="https://www.karabuk.edu.tr/en/";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAnns=findViewById(R.id.btn_Anns);
        btnNews=findViewById(R.id.btn_News);
        btnLinks=findViewById(R.id.btn_Links);
        btnOffices=findViewById(R.id.btn_Offices);
        tvTitle=findViewById(R.id.tv_Title);
        lvJsoup=findViewById(R.id.lv_Jsoup);
        btnLinks.setOnClickListener(this);
        btnAnns.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnOffices.setOnClickListener(this);
        queue = Volley.newRequestQueue(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        ArrayList<String> list=new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Document doc= Jsoup.parse(response);
                    int ID=view.getId();
                    switch (ID){
                        case R.id.btn_Anns:
                            tvTitle.setText("Duyurular");
                            Elements announces=doc.select("span.containerDuyuruBaslikLabel");
                            for (Element announce:announces)
                                list.add(announce.text());

                            break;
                        case R.id.btn_News:
                            tvTitle.setText("Haberler");
                            Elements news=doc.select("div.haberBoxHeader");
                            for (Element haber:news)
                                list.add(haber.text());

                            break;
                        case R.id.btn_Links:
                            tvTitle.setText("Linkler");
                            Elements linkler=doc.select("div.haberBoxHeader a");
                            for(Element link:linkler) {
                                list.add(link.attr("href"));
                            }
                            break;
                        case R.id.btn_Offices:
                            tvTitle.setText("Daire Başkanlıkları");
                            Elements daireler= doc.select("div.col-md-4 span");
                            for (Element daire : daireler) {
                                if(daire.text().equals("Administrative")){
                                    Element nextSibling=daire.nextElementSibling();
                                    Elements children=nextSibling.children();
                                    for(Element child:children){
                                        list.add(child.text());
                                    }
                                }
                            }
                            break;
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
                    lvJsoup.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Hata",error.getMessage());
            }
        });
        queue.add(request);

    }
}