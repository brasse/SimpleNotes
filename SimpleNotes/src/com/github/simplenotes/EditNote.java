package com.github.simplenotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditNote extends Activity {

    EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editnote);

        contentText = (EditText) findViewById(R.id.content);

        fillContent();
    }

    private void fillContent() {
        contentText.setText("foo bar baz...");
    }
}
