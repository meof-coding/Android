package com.example.myapplication;

import static com.example.myapplication.CalendarUtils.YearFromDate;
import static com.example.myapplication.CalendarUtils.daysInMonthArray;
import static com.example.myapplication.CalendarUtils.monthYearFromDate;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextView monthText;
    private TextView yearText;
    private RecyclerView calendarRecyclerView;
    //Init Button today-task
    private Button today_task;
    //Init Button completed-task
    private Button completed_task;

    public CalendarFragment() {
        // Required empty public constructor
    }

    //ImageView previous month
    private ImageView prevMonth;
    //ImageView next Month
    private ImageView nextMonth;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//   prev previous_month

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        //Get calendar Recycler View
        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        //Get month Text
        monthText = root.findViewById(R.id.MonthTV);
        //Get year Text
        yearText = root.findViewById(R.id.YearTV);
        //Get current date and assign to selected date
        CalendarUtils.selectedDate = LocalDate.now();
        //set month View
        setMonthView();
        //Pre-month onclick
        prevMonth = (ImageView) root.findViewById(R.id.previous_month);
        prevMonth.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
            yearText.setText(YearFromDate(CalendarUtils.selectedDate));
            setMonthView();
        });
        //nextMonth onclick
        nextMonth = (ImageView) root.findViewById(R.id.next_month);
        nextMonth.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            yearText.setText(YearFromDate(CalendarUtils.selectedDate));
            setMonthView();
        });
        //Today-task onClick
        today_task = (Button) root.findViewById(R.id.today_task);
        today_task.setOnClickListener(v -> {
            today_task.setBackgroundColor(today_task.getContext().getResources().getColor(R.color.primary));
            completed_task.setBackgroundColor(completed_task.getContext().getResources().getColor(R.color.gray));
            replaceFragment(new TodayTaskFragment());
        });
        //completed-task onClick
        completed_task = (Button) root.findViewById(R.id.completed_task);
        completed_task.setOnClickListener(v -> {
            completed_task.setBackgroundColor(completed_task.getContext().getResources().getColor(R.color.primary));
            today_task.setBackgroundColor(today_task.getContext().getResources().getColor(R.color.gray));
            replaceFragment(new CompletedTaskFragment());
        });
        return root;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_task, fragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
}