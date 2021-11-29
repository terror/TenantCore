package com.example.tenantcore.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.ActivityTenantCoreBinding;
import com.example.tenantcore.ui.tenant.home.TenantHomeFragment;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;

public class TenantCoreActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private ActivityTenantCoreBinding binding;
  private TenantCoreActivity context;
  private TenantCoreViewModel tenantCoreViewModel;

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
    // Display a logout warning if the currently displayed fragment is the tenant home fragment.
    if (getSupportFragmentManager().findFragmentByTag(TenantHomeFragment.TAG_NAME) == null || getSupportFragmentManager().findFragmentByTag(TenantHomeFragment.TAG_NAME).getView() == null)
      super.onBackPressed();
    else
      displayLogoutWarning();
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
      .setMessage("A new key will be required to access your account.\nWould you like to proceed?")
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
}
