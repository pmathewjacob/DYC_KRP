package com.android.app.dyc.krp.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.app.dyc.krp.models.RegisterUser;
import com.android.app.dyc.krp.R;

public class RegisterUserViewHolder extends RecyclerView.ViewHolder {

    private TextView nameView;
    private TextView phoneNumberView;

    public RegisterUserViewHolder(View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.register_user_fName);
        phoneNumberView = itemView.findViewById(R.id.register_user_phoneNumber);
    }

    public void bindToPost(RegisterUser registerUser) {
        nameView.setText(registerUser.fullName);
        phoneNumberView.setText(registerUser.phoneNumber);
    }
}
