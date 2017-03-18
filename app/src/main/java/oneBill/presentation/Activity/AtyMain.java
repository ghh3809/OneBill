package oneBill.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Book;
import oneBill.presentation.Account;
import oneBill.presentation.AddBook;
import oneBill.presentation.adapter.BookAdapter;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 主界面，即显示各账本的界面
 */
public class AtyMain extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageView imageviewMenu, imageviewHelp, imageviewAddBook;
    ListView listviewBook;
    Actioner actioner;
    ArrayList<Book> books;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        actioner = new Actioner(this);
        imageviewMenu = (ImageView) findViewById(R.id.imageviewMenu);
        imageviewHelp = (ImageView) findViewById(R.id.imageviewHelp);
        imageviewAddBook = (ImageView) findViewById(R.id.imageviewAddBook);
        listviewBook = (ListView) findViewById(R.id.listviewBook);
        imageviewMenu.setOnClickListener(this);
        imageviewHelp.setOnClickListener(this);
        imageviewAddBook.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        books = actioner.GetBooks();
        listviewBook.setAdapter(new BookAdapter(actioner, this, R.layout.book_data, books));
        listviewBook.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //菜单按钮
            case R.id.imageviewMenu:
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            //帮助按钮
            case R.id.imageviewHelp:
                Toast.makeText(AtyMain.this, "此处有教程，敬请期待！", Toast.LENGTH_SHORT).show();
                break;
            //添加账本按钮
            case R.id.imageviewAddBook:
                //TODO
//                Intent i = new Intent(AtyMain.this, AtyAddBook.class);
                Intent i = new Intent(AtyMain.this, AddBook.class);
                startActivity(i);
                break;
        }
    }

    //点击某账单
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.listviewBook:
                //TODO
                Intent intent = new Intent(AtyMain.this, Account.class);
                intent.putExtra("name", books.get(position).getName());
                startActivity(intent);
                break;
        }
    }
}
