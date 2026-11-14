package com.reversetalk.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private EditText input;
    private TextView output, status;
    private Spinner language;
    private SpeechRecognizer recognizer;
    private final String[] languageCodes = {"ar-DZ", "ar", "fr-FR", "en-US"};

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private Button button(String label, int color) {
        Button b = new Button(this); b.setText(label); b.setTextColor(Color.WHITE);
        b.setAllCaps(false); b.setTextSize(15); b.setBackground(bg(color, 14)); return b;
    }
    private void addGap(LinearLayout parent, int h) {
        Space s = new Space(this); parent.addView(s, new LinearLayout.LayoutParams(1, dp(h)));
    }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(11,18,32));
        getWindow().setNavigationBarColor(Color.rgb(11,18,32));
        buildUi();
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                public void onReadyForSpeech(Bundle p) { status.setText("أستمع الآن…"); }
                public void onBeginningOfSpeech() {}
                public void onRmsChanged(float r) {}
                public void onBufferReceived(byte[] b) {}
                public void onEndOfSpeech() { status.setText("جارٍ تحويل الصوت…"); }
                public void onError(int e) { status.setText("تعذّر التعرّف على الصوت. جرّب مرة أخرى أو اكتب النص."); }
                public void onResults(Bundle b) {
                    ArrayList<String> matches=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(matches!=null && !matches.isEmpty()) { input.append((input.length()>0?" ":"")+matches.get(0)); reverse(); status.setText("تم تحويل الصوت وعكس النص."); }
                }
                public void onPartialResults(Bundle b) {}
                public void onEvent(int t, Bundle p) {}
            });
        } else { status.setText("خدمة التعرّف على الصوت غير متوفرة على هذا الجهاز."); }
    }
    private void buildUi() {
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.setBackgroundColor(Color.rgb(11,18,32));
        LinearLayout page=new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL); page.setPadding(dp(20),dp(24),dp(20),dp(20));
        scroll.addView(page); setContentView(scroll);

        TextView logo=text("↔",34,Color.rgb(11,18,32),true); logo.setGravity(Gravity.CENTER);
        logo.setBackground(bg(Color.rgb(139,165,255),20));
        LinearLayout.LayoutParams lpLogo=new LinearLayout.LayoutParams(dp(68),dp(68)); lpLogo.gravity=Gravity.CENTER;
        page.addView(logo,lpLogo);
        addGap(page,12);
        TextView title=text("Reverse Talk",27,Color.WHITE,true); title.setGravity(Gravity.CENTER); page.addView(title);
        TextView sub=text("عاكس الكلام • Reverse your words",14,Color.LTGRAY,false); sub.setGravity(Gravity.CENTER); page.addView(sub);
        addGap(page,22);

        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(16),dp(16),dp(16),dp(16)); card.setBackground(bg(Color.rgb(21,31,50),20));
        page.addView(card,new LinearLayout.LayoutParams(-1,-2));
        card.addView(text("لغة التعرّف على الصوت",14,Color.LTGRAY,true));
        language=new Spinner(this);
        String[] langs={"العربية – الجزائر","العربية","Français","English"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,langs);
        language.setAdapter(adapter); card.addView(language);
        addGap(card,12);
        card.addView(text("الكلام الأصلي / Original text",14,Color.LTGRAY,true));
        input=new EditText(this); input.setTextColor(Color.WHITE); input.setHintTextColor(Color.GRAY);
        input.setHint("اكتب هنا أو استعمل الميكروفون…"); input.setTextSize(17);
        input.setGravity(Gravity.TOP|Gravity.RIGHT); input.setMinLines(4); input.setPadding(dp(12),dp(12),dp(12),dp(12));
        input.setBackground(bg(Color.rgb(13,23,40),12)); card.addView(input,new LinearLayout.LayoutParams(-1,dp(120)));
        addGap(card,12);
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button reverse=button("اعكس الكلام ↔",Color.rgb(104,137,230));
        Button mic=button("🎙 تحدث",Color.rgb(48,65,94));
        row.addView(reverse,new LinearLayout.LayoutParams(0,dp(52),1)); 
        LinearLayout.LayoutParams m=new LinearLayout.LayoutParams(0,dp(52),1); m.setMargins(dp(8),0,0,0); row.addView(mic,m);
        card.addView(row);
        addGap(card,8);
        status=text("جاهز",12,Color.LTGRAY,false); card.addView(status);

        addGap(page,14);
        LinearLayout resultCard=new LinearLayout(this); resultCard.setOrientation(LinearLayout.VERTICAL); resultCard.setPadding(dp(16),dp(16),dp(16),dp(16)); resultCard.setBackground(bg(Color.rgb(21,31,50),20));
        page.addView(resultCard,new LinearLayout.LayoutParams(-1,-2));
        resultCard.addView(text("النتيجة المعكوسة / Reversed result",14,Color.LTGRAY,true));
        output=text("ستظهر النتيجة هنا…",20,Color.WHITE,false); output.setGravity(Gravity.RIGHT); output.setPadding(0,dp(14),0,dp(14));
        resultCard.addView(output,new LinearLayout.LayoutParams(-1,-2));
        LinearLayout actions=new LinearLayout(this);
        Button copy=button("نسخ النتيجة",Color.rgb(48,65,94)), clear=button("مسح الكل",Color.rgb(48,65,94));
        actions.addView(copy,new LinearLayout.LayoutParams(0,dp(50),1));
        LinearLayout.LayoutParams c=new LinearLayout.LayoutParams(0,dp(50),1); c.setMargins(dp(8),0,0,0); actions.addView(clear,c);
        resultCard.addView(actions);
        addGap(page,16);
        TextView foot=text("التعرّف على الصوت يعتمد على خدمات Google/الجهاز وقد يحتاج اتصالاً بالإنترنت.",12,Color.LTGRAY,false);
        foot.setGravity(Gravity.CENTER); page.addView(foot);

        reverse.setOnClickListener(v->reverse());
        mic.setOnClickListener(v->startSpeech());
        clear.setOnClickListener(v->{input.setText("");output.setText("ستظهر النتيجة هنا…");status.setText("تم المسح.");});
        copy.setOnClickListener(v->{
            android.content.ClipboardManager cb=(android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            cb.setPrimaryClip(android.content.ClipData.newPlainText("Reverse Talk",output.getText()));
            status.setText("تم نسخ النتيجة.");
        });
    }
    private void reverse() {
        String s=input.getText().toString();
        BreakIterator it=BreakIterator.getCharacterInstance(new Locale("ar"));
        it.setText(s); ArrayList<String> parts=new ArrayList<>();
        int start=it.first(), end=it.next();
        while(end!=BreakIterator.DONE){parts.add(s.substring(start,end));start=end;end=it.next();}
        StringBuilder out=new StringBuilder(); for(int i=parts.size()-1;i>=0;i--) out.append(parts.get(i));
        output.setText(out.toString()); status.setText("تم عكس النص.");
    }
    private void startSpeech() {
        if(recognizer==null){status.setText("التعرّف على الصوت غير متوفر.");return;}
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},42); return;
        }
        String code=languageCodes[language.getSelectedItemPosition()];
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,code);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,code);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        recognizer.startListening(intent);
    }
    @Override public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] results){
        super.onRequestPermissionsResult(requestCode,permissions,results);
        if(requestCode==42 && results.length>0 && results[0]==PackageManager.PERMISSION_GRANTED) startSpeech();
        else status.setText("يجب السماح باستعمال الميكروفون.");
    }
    @Override protected void onDestroy(){ if(recognizer!=null){recognizer.destroy();recognizer=null;} super.onDestroy(); }
}
