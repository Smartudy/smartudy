package com.sharewith.smartudy.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.adapter.WriteFragmentRecyclerAdapter;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class WriteFragment extends Fragment{

    private CoordinatorLayout mFragmentWrite;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WriteFragmentListener mQnAActivity;
    private FloatingActionButton main_fab;
    private Animation fab_open_anim,fab_close_anim,fab_rotate_forward,fab_rotate_backward;
    private ImageView bottom1,bottom2,bottom3,bottom4,bottom5;
    private Write_DBhelper mDBhelper;
    private RecyclerView mRecycler;
    private EditText mTitle;
    private String mPictureName;
    private String mPicturePath;
    private Uri mPictureUri; //FileProvider가 사용할 이미지 파일에 대한 content uri
    private boolean is_fabopen;
    private static final int REQUEST_PICTURE = 0; //private static으로 선언하여 인스턴스끼리만 값을 공유하게끔.
    private static final int REQUEST_ALBUM = 1;
    private static final int REQUEST_CROP = 2;

    public static interface WriteFrag_To_QnAActivity{
        public void addNotePad(NotePadDto notepad);
        //WriteFragment의 + 버튼 눌렀을때 질문&답변창에 업데이트 하기 위해서
    }

    @Override
    public void onAttach(Context context) {
        try{
            mQnAActivity = (WriteFragmentListener)context;
            //만약 Fragment와 연결된 액티비티(context)에서 WriteFragmentListener를 구현하지 않았다면 여기서 예외 발생
        }catch(ClassCastException e){
            Log.d("Log",context+"'s Listener is not yet implemented!!");
        }
        super.onAttach(context);
    }

    public static interface WriteFragmentListener{
//         void setTransParentBackground();
//         void UnsetTransParentBackground();
    }

    private void setMember(View view){
        mTitle = view.findViewById(R.id.write_title);
        mRecycler = view.findViewById(R.id.write_fragment_recycler);
        mRecycler.setAdapter(new WriteFragmentRecyclerAdapter(getContext()));
        mDBhelper = new Write_DBhelper(view.getContext());
//        mFragmentWrite = view.findViewById(R.id.fragment_write);
//        mFragmentWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(is_fabopen){
//                    animation();
//                }
//            }
//        });
        fm = getActivity().getSupportFragmentManager();
        main_fab = view.findViewById(R.id.main_write_fab);
        fab_open_anim = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fab_close_anim = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        fab_rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        fab_rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        bottom1 = view.findViewById(R.id.write_fragment_bottom1);
        bottom2 = view.findViewById(R.id.write_fragment_bottom2);
        bottom3 = view.findViewById(R.id.write_fragment_bottom3);
        bottom4 = view.findViewById(R.id.write_fragment_bottom4);
        bottom5 = view.findViewById(R.id.write_fragment_bottom5);
        is_fabopen = true;
    }

    private void setListener(){
        main_fab.setOnClickListener(onClickListener);
        bottom1.setOnClickListener(onClickListener);
        bottom2.setOnClickListener(onClickListener);
        bottom3.setOnClickListener(onClickListener);
        bottom4.setOnClickListener(onClickListener);
        bottom5.setOnClickListener(onClickListener);
    }
//    private void setFAB(View view){
//        mfab1.setOnClickListener(onClickListener);
//        mfab2.setOnClickListener(onClickListener);
//        mfab3.setOnClickListener(onClickListener);
//        mfab4.setOnClickListener(onClickListener);
//        background.setBackgroundColor(Color.parseColor("#994C4C4C"));
//        animation();
//    }

    private String getText(){
        String contents = "";
        for(int i=0; i<mRecycler.getChildCount(); i++){
//            RecyclerView.ViewHolder viewHolder = mRecycler.findViewHolderForAdapterPosition(i);
//            if(viewHolder instanceof WriteFragmentRecyclerAdapter.TextViewHolder)
//                contents += ((WriteFragmentRecyclerAdapter.TextViewHolder) viewHolder).editText.getText().toString();
        }
        return contents;
    }

    private void setNotePad(NotePadDto notepad){ //디비에 업데이트 할때 사용됨.
        String title = mTitle.getText().toString();
        String contents = getText();
        notepad.setTitle(title);
        notepad.setContents(contents);
    }




    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.main_write_fab:
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
                    //키보드 내리기
                    NotePadDto notepad = new NotePadDto();
                    setNotePad(notepad); //WriteFragment에 있는 모든 정보를 긁어와서 notepad객체에 설정
                    //디비 업데이트
                    mDBhelper.insertNotePad(notepad);
                    ((QnAActivity)getActivity()).addNotePad(notepad);
                    //QnAListFragment's 리싸이클러뷰 어댑터 갱신
                    //WriteFragment->QnAActivity->QnAListFragment순서로 전달됨.
                    break;
                case R.id.write_fragment_bottom1:
                    ((WriteFragmentRecyclerAdapter)mRecycler.getAdapter()).addView(WriteFragmentRecyclerAdapter.STATE_TEXT,"");
                    mRecycler.smoothScrollToPosition(mRecycler.getChildCount());
                    break;
                case R.id.write_fragment_bottom2:
                    selectPhoto();
                    break;
                case R.id.write_fragment_bottom3:
                    selectAlbum();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file;
        Intent intent;
                if(resultCode == RESULT_OK){
                    switch(requestCode){
                        case REQUEST_PICTURE:
                            crop();
                            break;
                        case REQUEST_ALBUM:
                            Uri srcUri = data.getData(); //선택된 이미지의 content uri
                            File srcFile = new File(getRealPathFromURI(srcUri)); //선택된 이미지 파일
                            File destFile = createImageFile();
                            try{
                                copyFile(srcFile,destFile); //갤러리에 있는 파일을 내 외장 메모리로 가져와야 크롭 기능을 수행할수있다.
                                mPictureUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),getActivity().getPackageName()+".fileprovider",destFile);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            crop();
                            break;
                        case REQUEST_CROP:
                            ((WriteFragmentRecyclerAdapter)mRecycler.getAdapter()).addView(WriteFragmentRecyclerAdapter.STATE_SHOT,mPicturePath);
                            break;

            }
        }
    }

    //    public void animation(){
//            if(is_fabopen){//닫으려고 할때
//                main_fab.startAnimation(fab_rotate_backward);
//                mfab1.startAnimation(fab_close_anim);
//                mfab2.startAnimation(fab_close_anim);
//                mfab3.startAnimation(fab_close_anim);
//                mfab4.startAnimation(fab_close_anim);
//                mfab1.setClickable(false);mfab2.setClickable(false);
//                mfab3.setClickable(false);mfab4.setClickable(false);
//                background.setVisibility(View.GONE);
//                is_fabopen = false;
//            }else{//열려고 할때
//                main_fab.startAnimation(fab_rotate_forward);
//                mfab1.startAnimation(fab_open_anim);
//                mfab2.startAnimation(fab_open_anim);
//                mfab3.startAnimation(fab_open_anim);
//                mfab4.startAnimation(fab_open_anim);
//                mfab1.setClickable(true);mfab2.setClickable(true);
//                mfab3.setClickable(true);mfab4.setClickable(true);
//                background.setVisibility(View.VISIBLE);
//                is_fabopen = true;
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        setMember(view);
        setListener();
       super.onCreateView(inflater, container, savedInstanceState);
       return view;
    }

    //여기부터는 사진 및 비트맵 처리 함수들

    private void crop(){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mPictureUri, "image/*");
        //Android N need set permission to uri otherwise system camera don't has permission to access file wait crop
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //전달된 Content URI를 카메라 앱이 read/write할 수 있는 권한.
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
        startActivityForResult(intent,REQUEST_CROP);
    }

    private void selectAlbum(){ //갤러리에서 사진 가져오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_ALBUM);
        }
    }
    private void selectPhoto() { //사진 찍고 가져오기
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){ //외장 메모리가 제대로 연결 되었는가?
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //카메라 앱 실행
            if(intent.resolveActivity(getActivity().getPackageManager()) != null){ //인텐트를 처리할수 있는 앱이 있는가 확인
                File photoFile = null;
                try {
                    photoFile = createImageFile(); //외장 메모리의 공유 폴더에 파일 객체 생성
                    mPictureUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                    Log.d("LOG","Photo's Content URI is "+mPictureUri);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //content://com.sharewith.smartudy.fileprovider/my_images/20180723_100001.png content uri생성
                intent.putExtra(MediaStore.EXTRA_OUTPUT,mPictureUri);
                startActivityForResult(intent,REQUEST_PICTURE);
            }
        }
    }//이 함수를 호출하면 photoFile의 위치에 찍은 사진이 담기게 된다.

    //Content Uri를 절대경로로 바꿔주는 함수
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        FileChannel destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    private File createImageFile(){
        File publicdir = new File(Environment.getExternalStorageDirectory(),"smartudy"); //외부 스토리지 공유 폴더 객체 생성
        if(!publicdir.exists()){ //공유 폴더가 존재하지 않는다면
            publicdir.mkdirs(); //경로에 포함된 모든 디렉토리 생성
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mPictureName = timestamp + ".jpg"; //20180330_123030.png와 같이 이미지 파일 생성하기 위해서

        File Photo = new File(publicdir,mPictureName);
        //아직 파일이 생성된것은 아니고 파일 객체만 생성된 상태이다.
        mPicturePath = Photo.getAbsolutePath();
        return Photo;
    }



}
