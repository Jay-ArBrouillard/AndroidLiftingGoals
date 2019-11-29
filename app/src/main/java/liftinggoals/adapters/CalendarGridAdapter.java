package liftinggoals.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.liftinggoals.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import liftinggoals.calendar.Event;

public class CalendarGridAdapter extends ArrayAdapter {
    private List<Date> dates;
    private Calendar currentDate;
    private List<Event> events;
    private LayoutInflater inflater;

    public CalendarGridAdapter(@Nullable Context context, List<Date> dates, Calendar currentDate, List<Event> events) {
        super(context, R.layout.calendar_single_cell);
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);

        if (events == null)
        {
            this.events = new ArrayList<>();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if (view == null)
        {
            view = inflater.inflate(R.layout.calendar_single_cell, parent, false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear)
        {
            view.setBackgroundColor(Color.parseColor("#86A8E7")); //Light blue
        }
        else
        {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView day_number = view.findViewById(R.id.calendar_day);
        TextView eventNumber = view.findViewById(R.id.events_id);
        day_number.setText(String.valueOf(DayNo));
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < events.size(); i++)
        {
            dateCalendar.setTime(convertStringToDate(events.get(i).getDATE()));
            if (DayNo == dateCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == dateCalendar.get(Calendar.MONTH) + 1
                && displayYear == dateCalendar.get(Calendar.YEAR))
            {
                arrayList.add(events.get(i).getEVENT());
                eventNumber.setText(arrayList.size()+ " Events");
            }
        }

        return view;
    }

    private Date convertStringToDate(String eventDate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try
        {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getCount() {
        return dates.size();
    }
}
