package com.example.tenantcore.ui;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.ActivityTenantCoreBinding;
import com.example.tenantcore.ui.landlord.home.LandlordHomeFragment;
import com.example.tenantcore.ui.tenant.home.TenantHomeFragment;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;

public class TenantCoreActivity extends AppCompatActivity {
  public static final String REQUESTS_NOTIFICATION_CHANNEL = "requests-notification-channel";
  public static final String REQUESTS_NOTIFICATION_GROUP = "requests-notifications";
  public static final int PICK_IMAGE = 1;

  private AppBarConfiguration appBarConfiguration;
  private ActivityTenantCoreBinding binding;
  private TenantCoreActivity context;
  private final TenantCoreViewModel tenantCoreViewModel;

  // Microphone settings
  public static final Integer RecordAudioRequestCode = 1;
  private SpeechRecognizer speechRecognizer;

  public TenantCoreActivity() {
    tenantCoreViewModel = new TenantCoreViewModel();
  }

  // Note: The database cannot be created unless the context has a generated fragment.
  // This is why this method is called from the Home Fragment.
  public void initializeViewModel(TenantCoreActivity context) {
    try {
      tenantCoreViewModel.initializeDatabase(context);
    } catch (Exception e) {
      System.err.println("Error loading view model.");
    }
  }

  public TenantCoreViewModel getTenantViewModel() {
    return this.tenantCoreViewModel;
  }

  public void setSpeechRecognizer(SpeechRecognizer speechRecognizer) {
    this.speechRecognizer = speechRecognizer;
  }

  public SpeechRecognizer getSpeechRecognizer() {
    return this.speechRecognizer;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;

    binding = ActivityTenantCoreBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    // Create the notification channel
    createNotificationChannel();

    // Process intent
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      // Navigate to the RequestListFragment
      NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      controller.navigate(R.id.action_LandlordHomeFragment_to_RequestListFragment);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);

    binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        context.onBackPressed();
      }
    });

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings)
      return true;

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
      || super.onSupportNavigateUp();
  }

  @Override
  public void onBackPressed() {
    // Display a logout warning if the currently displayed fragment is the tenant home fragment or the landlord home fragment
    Fragment tenantHomeFragment = getSupportFragmentManager().findFragmentByTag(TenantHomeFragment.TAG_NAME);
    Fragment landlordHomeFragment = getSupportFragmentManager().findFragmentByTag(LandlordHomeFragment.TAG_NAME);
    if ((tenantHomeFragment == null || tenantHomeFragment.getView() == null) && (landlordHomeFragment == null || landlordHomeFragment.getView() == null))
      super.onBackPressed();
    else
      displayLogoutWarning();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // If they picked an image, set the image uri
    if (requestCode == PICK_IMAGE) {
      Uri imageUri = data.getData();
      tenantCoreViewModel.setImageUri(imageUri);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    speechRecognizer.destroy();
  }

  public void displayErrorMessage(String title, String msg) {
    new AlertDialog.Builder(this)
      .setIcon(R.drawable.ic_baseline_warning_24)
      .setTitle(title)
      .setMessage(msg)
      .setPositiveButton("OK", null)
      .create()
      .show();
  }

  public void displayLogoutWarning() {
    new AlertDialog.Builder(this)
      .setIcon(R.drawable.ic_baseline_warning_24)
      .setTitle("Logout")
      .setMessage("Returning to home will log you out of your account.\n\nWould you like to proceed?")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          TenantCoreActivity.super.onBackPressed();
        }
      })
      .setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      })
      .create()
      .show();
  }

  private void createNotificationChannel() {
    String name = "Requests";
    String description = "Notifications concerning Requests that are overdue";

    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    NotificationChannel channel = new NotificationChannel(REQUESTS_NOTIFICATION_CHANNEL, name, importance);
    channel.setDescription(description);

    // Register the channel with the system; you can't change the importance
    // or other notification behaviors after this
    NotificationManager notificationManager = getSystemService(NotificationManager.class);
    notificationManager.createNotificationChannel(channel);
  }

  public void hideKeyboard() {
    View view = this.getCurrentFocus();

    // If there is currently a view in focus, hide the keyboard
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}
