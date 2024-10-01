package com.orhotech.customchips;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.orhotech.multichiptextview.ChipConfiguration;
import com.orhotech.multichiptextview.MultiChipsTextView;
import com.orhotech.multichiptextview.chip.ChipSpan;
import com.orhotech.multichiptextview.chip.ChipSpanChipCreator;
import com.orhotech.multichiptextview.terminator.ChipTerminatorHandler;
import com.orhotech.multichiptextview.tokenizer.SpanChipTokenizer;
import com.orhotech.multichiptextview.validator.ChipifyingMultiChipValidator;
import com.orhotech.multichiptextview.validator.IllegalCharacterIdentifier;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MultiChip";
    private static final String[] SUGGESTIONS = new String[]{"MultiChip", "Chip", "Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Cheddar", "Mozzarella", "Mexico", "Jalapeno"};

    TextView mInfoBodyView;
    MultiChipsTextView mMultiChipTextView;
    MultiChipsTextView mMultiChipTextViewWithIcons;
    Button mListChipValues,mListChipValuesToken,mToString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mInfoBodyView = findViewById(R.id.info_body);
        mMultiChipTextView = findViewById(R.id.multi_chip_text_view);
        mMultiChipTextViewWithIcons = findViewById(R.id.multi_chip_text_view_with_icons);

        mListChipValues = findViewById(R.id.list_chip_values);
        mListChipValuesToken = findViewById(R.id.list_chip_and_token_values);
        mToString = findViewById(R.id.to_string);

        mListChipValues.setOnClickListener(this::listChipValues);
        mListChipValuesToken.setOnClickListener(this::listChipAndTokenValues);
        mToString.setOnClickListener(this::toastToString);

        Spanned infoText;
        infoText = Html.fromHtml(getString(R.string.info_text_body), Html.FROM_HTML_MODE_LEGACY);
        mInfoBodyView.setText(infoText);

        setupChipTextView(mMultiChipTextView);
        setupChipTextView(mMultiChipTextViewWithIcons);

        List<String> testList = new ArrayList<>();
        testList.add("testing");
        testList.add("setText");
        mMultiChipTextView.setText(testList);

        mMultiChipTextViewWithIcons.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {
            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                return new ChipSpan(context, text, ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher), data);
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
            }
        }, ChipSpan.class));
    }

    private void setupChipTextView(MultiChipsTextView multiChipTextView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, SUGGESTIONS);
        multiChipTextView.setAdapter(adapter);
        multiChipTextView.setIllegalCharacterIdentifier(new IllegalCharacterIdentifier() {
            @Override
            public boolean isCharacterIllegal(Character c) {
                return !c.toString().matches("[a-z0-9 ]");
            }
        });
        multiChipTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        multiChipTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        multiChipTextView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        multiChipTextView.setMultiChipValidator(new ChipifyingMultiChipValidator());
        multiChipTextView.enableEditChipOnTouch(true, true);
        multiChipTextView.setOnChipClickListener((chip, motionEvent) -> Log.d(TAG, "onChipClick: " + chip.getText()));
        multiChipTextView.setOnChipRemoveListener(chip -> {
            Log.d(TAG, "onChipRemoved: " + chip.getText());
            mMultiChipTextView.setSelection(mMultiChipTextView.getText().length());
        });
    }


    public void listChipValues(View view) {
        List<String> chipValues = mMultiChipTextView.getChipValues();
        alertStringList("Chip Values", chipValues);
    }

    public void listChipAndTokenValues(View view) {
        List<String> chipAndTokenValues = mMultiChipTextView.getChipAndTokenValues();
        alertStringList("Chip and Token Values", chipAndTokenValues);
    }


    public void toastToString(View view) {
        List<String> strings = new ArrayList<>();
        strings.add(mMultiChipTextView.toString());
        alertStringList("toString()", strings);
    }

    private void alertStringList(String title, List<String> list) {
        String alertBody;
        if (!list.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String chipValue : list) {
                builder.append(chipValue);
                builder.append("\n");
            }
            builder.deleteCharAt(builder.length() - 1);
            alertBody = builder.toString();
        } else {
            alertBody = "No strings";
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertBody)
                .setCancelable(true)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();
    }


}