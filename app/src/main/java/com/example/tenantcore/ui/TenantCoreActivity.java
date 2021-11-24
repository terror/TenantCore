package com.example.tenantcore.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.ActivityTenantCoreBinding;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;

public class TenantCoreActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private ActivityTenantCoreBinding binding;
  private TenantCoreViewModel tenantCoreViewModel;

  public TenantCoreActivity(){
    this.tenantCoreViewModel = new TenantCoreViewModel();
  }

  public TenantCoreViewModel getTaskViewModel(){
    return this.tenantCoreViewModel;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
}
