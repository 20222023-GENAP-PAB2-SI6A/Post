package masterous.si6a.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.List;

import masterous.si6a.post.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private UnggahViewAdapter unggahViewAdapter;
    private List<Unggah> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!Utilities.checkValue(MainActivity.this, "xUsername")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        unggahViewAdapter = new UnggahViewAdapter();
        binding.rvUnggah.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUnggah.setAdapter(unggahViewAdapter);
        unggahViewAdapter.setOnItemLongClickListener(new UnggahViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, Unggah unggah, int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setGravity(Gravity.RIGHT);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int idMenu = item.getItemId();
                        if (idMenu == R.id.action_edit) {
                            Intent intent = new Intent(MainActivity.this, UpdateUnggahActivity.class);
                            intent.putExtra("EXTRA_DATA", unggah);
                            startActivity(intent);
                            return true;
                        } else if (idMenu == R.id.action_delete) {
                            String id = unggah.getId();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Konfirmasi");
                            builder.setMessage("Yakin ingin menghapus unggah '" + data.get(position).getContent() + "' ?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUnggah(id);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
        });

        binding.fabInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUnggahActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteUnggah(String id) {
        // memanggil API untuk delete
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllUnggah();
    }

    private void getAllUnggah() {
        binding.progressBar.setVisibility(View.VISIBLE);
        // memanggil data unggah dari server...
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Utilities.clearUser(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}