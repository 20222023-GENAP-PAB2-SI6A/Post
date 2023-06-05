package masterous.si6a.post;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import masterous.si6a.post.databinding.ActivityAddUnggahBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUnggahActivity extends AppCompatActivity {
    private ActivityAddUnggahBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUnggahBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.etContent.getText().toString();

                boolean bolehUnggah = true;

                if (TextUtils.isEmpty(content)) {
                    bolehUnggah = false;
                    binding.etContent.setError("Konten tidak boleh kosong!");
                }

                if (bolehUnggah) {
                    String userId = Utilities.getValue(AddUnggahActivity.this, "xUserId");
                    addUnggah(content, userId);
                }
            }
        });
    }

    private void addUnggah(String content, String userId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utilities.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.addUnggah(content, userId);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success == 1) {
                        Toast.makeText(AddUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddUnggahActivity.this, "Response " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : " + t.getMessage());
                Toast.makeText(AddUnggahActivity.this, "Retrofit Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}