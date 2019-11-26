package liftinggoals.calendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.liftinggoals.R;
import com.google.android.gms.location.LocationCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import liftinggoals.adapters.CalendarGridAdapter;
import liftinggoals.data.DatabaseHelper;

public class CustomCalendarView extends LinearLayout {
    private static final int MAX_CALENDAR_DAYS = 42;
    private ImageButton nextButton, previousButton;
    private TextView currentDate;
    private GridView gridView;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    private Context context;
    private List<Date> dates = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private CalendarGridAdapter calendarGridAdapter;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeLayout();
        setupCalendar();

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setupCalendar();
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setupCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                view.setBackgroundColor(Color.TRANSPARENT);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_add_new_event, parent, false);
                addView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //TODO fix background transparent
                final EditText eventName = addView.findViewById(R.id.event_name);
                final TextView eventTime = addView.findViewById(R.id.event_time);
                ImageButton setTime = addView.findViewById(R.id.set_event_time);
                final Button addEventButton = addView.findViewById(R.id.add_event);
                setTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance(Locale.ENGLISH);
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String event_Time = hformat.format(c.getTime());
                                eventTime.setText(event_Time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                addEventButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent(eventName.getText().toString(), eventTime.getText().toString(), date, month, year);
                        setupCalendar();
                    }
                });

                builder.setView(addView);
                builder.create();
                builder.show();
            }
        });
    }

    private void saveEvent(String event, String time, String date, String month, String year)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        db.openDB();
        db.insertEvent(event, time, date, month, year);
        db.closeDB();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void initializeLayout()
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = view.findViewById(R.id.calendarPreviousBtn);
        nextButton = view.findViewById(R.id.calendarNextBtn);
        currentDate = view.findViewById(R.id.calendarCurrentDate);
        gridView = view.findViewById(R.id.history_activity_grid_view);
    }

    private void setupCalendar()
    {
        String stringCurrDate = dateFormat.format(calendar.getTime());
        currentDate.setText(stringCurrDate);
        //Setup dates
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        CollectEventsForMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size()  < MAX_CALENDAR_DAYS)
        {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarGridAdapter = new CalendarGridAdapter(context, dates, calendar, eventList);
        gridView.setAdapter(calendarGridAdapter);
    }

    private void CollectEventsForMonth(String month, String year)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        db.openDB();
        eventList = db.getEventsByMonthAndYear(month, year);
        db.closeDB();
    }
}
