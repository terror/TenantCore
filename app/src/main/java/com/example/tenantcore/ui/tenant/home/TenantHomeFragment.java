package com.example.tenantcore.ui.tenant.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentTenantHomeBinding;
import com.example.tenantcore.model.PlaceholderContent;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.ui.util.DatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TenantHomeFragment extends Fragment {

  public static String TAG_NAME = "tenant_home";

  private FragmentTenantHomeBinding binding;
  private TenantCoreActivity activity;
  private Calendar requestDate;
  private SimpleDateFormat formatter;

  private boolean speechRecognitionAvailable = false;
  private Intent speechRecognizerIntent;
  private EditText[] speechEditTexts;
  private String[] speechEditTextHints;
  private int currentSpeechIndex = 0;
  private boolean isStartingSpeech = true;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    activity = (TenantCoreActivity)getActivity();

    if (activity.getSupportFragmentManager().findFragmentByTag(TAG_NAME) == null)
      activity.getSupportFragmentManager().beginTransaction().add(this, TAG_NAME).commit();

    binding = FragmentTenantHomeBinding.inflate(inflater, container, false);
    formatter = new SimpleDateFormat("EEEE, MMMM d");
    setListeners();

    binding.speechImgBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!speechRecognitionAvailable) {
          speechRecognitionAvailable = checkSpeechRecognitionAvailability();
          if (!speechRecognitionAvailable)
            return;
        }
        if (speechEditTexts == null)
          setupMicrophone();

        activity.getSpeechRecognizer().startListening(speechRecognizerIntent);
      }
    });

    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void setupMicrophone() {
    // Insert EditText that will be modified
    speechEditTexts = new EditText[] {
      binding.requestTitleEditText,
      binding.requestDescEditText
    };
    speechEditTextHints = new String[] {
      binding.requestTitleEditText.getText().toString(),
      binding.requestDescEditText.getText().toString(),
    };

    activity.setSpeechRecognizer(SpeechRecognizer.createSpeechRecognizer(activity));
    speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

    activity.getSpeechRecognizer().setRecognitionListener(new RecognitionListener() {
      @Override
      public void onReadyForSpeech(Bundle bundle) {
        binding.speechImgBtn.setImageResource(R.drawable.ic_baseline_mic_on_24);
        speechEditTexts[currentSpeechIndex].setText("");
        speechEditTexts[currentSpeechIndex].setHint("Ready");
        isStartingSpeech = false;
      }

      @Override
      public void onBeginningOfSpeech() {
        speechEditTexts[currentSpeechIndex].setHint("Listening...");
      }

      @Override
      public void onRmsChanged(float v) { }

      @Override
      public void onBufferReceived(byte[] bytes) { }

      @Override
      public void onEndOfSpeech() { }

      @Override
      public void onError(int i) {
        if (isStartingSpeech)
          return;

        speechEditTexts[currentSpeechIndex].setText(R.string.speech_error);
        stopSpeechRecognition();
      }

      @Override
      public void onResults(Bundle bundle) {
        processSpeechRecognitionResult(bundle);
        stopSpeechRecognition();
        if (++currentSpeechIndex >= speechEditTexts.length) {
          currentSpeechIndex = 0;
        }
      }

      @Override
      public void onPartialResults(Bundle bundle) {
        processSpeechRecognitionResult(bundle);
      }

      @Override
      public void onEvent(int i, Bundle bundle) { }
    });
  }

  private boolean checkSpeechRecognitionAvailability() {
    // Check if the device supports speech recognition
    if (!SpeechRecognizer.isRecognitionAvailable(activity)) {
      activity.displayErrorMessage("Speech Recognition not supported",
        "Your device does not support speech recognition." +
          "Consider downloading the Google app on the Google Play store.");
      return false;
    }

    // Request permission for speech recognition
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, TenantCoreActivity.RecordAudioRequestCode);
      return ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }
    return true;
  }

  private void processSpeechRecognitionResult(Bundle bundle) {
    String text = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
    String capitalizedText = text.substring(0, 1).toUpperCase() + text.substring(1);
    speechEditTexts[currentSpeechIndex].setText(capitalizedText);
  }

  private void stopSpeechRecognition() {
    activity.getSpeechRecognizer().stopListening();
    speechEditTexts[currentSpeechIndex].setHint(speechEditTextHints[currentSpeechIndex]);
    binding.speechImgBtn.setImageResource(R.drawable.ic_baseline_mic_off_24);
    isStartingSpeech = true;
  }

  /**
   * Clears the data from all of the request form fields.
   */
  private void resetForm() {
    binding.requestTitleEditText.getText().clear();
    binding.requestDescEditText.getText().clear();
    resetDate();
    resetUrgency();
  }

  /**
   * Clears the request forms urgency field.
   */
  private void resetUrgency() {
    binding.urgencyLowRadioBtn.setChecked(false);
    binding.urgencyMedRadioBtn.setChecked(false);
    binding.urgencyHighRadioBtn.setChecked(false);
  }

  /**
   * Clears the request forms date field.
   */
  private void resetDate() {
    binding.requestDateBtn.setText("");
    requestDate = null;
  }

  /**
   * Opens a date picker to allow for a request date to be selected.
   */
  private void chooseDate() {
    // Create a date picker fragment and pass in the default date.
    DatePickerDialogFragment datePicker = DatePickerDialogFragment.create(
      getDefaultDueDate(), new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int date) {
          setRequestDate(year, month, date);
        }
      });

    datePicker.show(getFragmentManager(), "datePicker");
  }

  /**
   * Sets the request date using the provided year, month, and date.
   * @param year The year to set the request date to.
   * @param month The month to set the request date to.
   * @param date The date to set the request date to.
   */
  private void setRequestDate(int year, int month, int date) {
    if (requestDate == null)
      requestDate = Calendar.getInstance();

    requestDate.set(Calendar.YEAR, year);
    requestDate.set(Calendar.MONTH, month);
    requestDate.set(Calendar.DATE, date);

    validateDate();
  }

  /**
   * Validates the current request date.
   */
  private void validateDate() {
    Calendar today = Calendar.getInstance();

    /*
     Display an alert if the chosen date is invalid (before today).
     Otherwise, set the date button text to request date.
     */
    if (requestDate.before(today)) {
      requestDate = null;

      new AlertDialog.Builder(getContext())
        .setTitle("Invalid Date.")
        .setMessage("Please select a date in the future.")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton("OK", null)
        .show();
    }
    else
      binding.requestDateBtn.setText(formatter.format(requestDate.getTime()));
  }

  /**
   * Submits a new request using the data provided in the request form.
   */
  private void submitRequest() {
    if (!validRequest())
      return;

    // Create a new request
    Request request = new Request(
      binding.requestTitleEditText.getText().toString(),
      binding.requestDescEditText.getText().toString(),
      requestDate != null ? requestDate.getTime() : null);

    // Set the requests urgency
    if (binding.urgencyLowRadioBtn.isChecked())
      request.setPriority(Priority.LOW);
    else if (binding.urgencyMedRadioBtn.isChecked())
      request.setPriority(Priority.MEDIUM);
    else if (binding.urgencyHighRadioBtn.isChecked())
      request.setPriority(Priority.HIGH);

    //TODO: Add the request to the database

    resetForm();
  }

  /**
   * Validates the request by ensuring that the
   * title and description fields are filled.
   * @return True if the request is valid, false otherwise.
   */
  private boolean validRequest() {
    // If both the request title and description are not empty, return true.
    if (binding.requestTitleEditText.length() != 0 && binding.requestDescEditText.length() != 0)
      return true;

    // Display an alert if the either of the fields are empty.
    new AlertDialog.Builder(getContext())
      .setTitle("Invalid request.")
      .setMessage("A request must have a title and description.")
      .setIcon(android.R.drawable.ic_dialog_alert)
      .setPositiveButton("OK", null)
      .show();

    return false;
  }

  /**
   * Gets the default date for a request.
   * By default this is a week after today's date.
   * @return The default request date.
   */
  private Date getDefaultDueDate() {
    Date defaultDate = new Date();
    defaultDate.setDate(defaultDate.getDate() + 7); // Advance today's date by 7 days

    return defaultDate;
  }

  /**
   * Sets all of the listeners for this fragment.
   */
  private void setListeners() {
    // Navigate to the request list fragment when the view request button is clicked.
    binding.viewRequestsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Get a place holder tenant and set it in the view-model.
        PlaceholderContent.PlaceholderItem tenant = PlaceholderContent.ITEMS.get(0);
        ((TenantCoreActivity)getActivity()).getTenantViewModel().setTenant(tenant);

        NavHostFragment.findNavController(TenantHomeFragment.this)
          .navigate(R.id.action_TenantHomeFragment_to_RequestListFragment);
      }
    });

    // Clear all request form fields when the reset button is clicked.
    binding.formResetBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetForm();
      }
    });

    // Clear the request form urgency field when the clear urgency button is clicked.
    binding.urgencyClearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetUrgency();
      }
    });

    // Clear the request form date field when the clear date button is clicked.
    binding.dateClearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetDate();
      }
    });

    // Open the date picker fragment when the date button is clicked.
    binding.requestDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        chooseDate();
      }
    });

    // Submit the request when the submit button is clicked.
    binding.formSubmitBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        submitRequest();
      }
    });
  }
}
