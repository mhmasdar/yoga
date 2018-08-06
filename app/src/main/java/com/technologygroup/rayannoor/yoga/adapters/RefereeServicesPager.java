package com.technologygroup.rayannoor.yoga.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.technologygroup.rayannoor.yoga.referees.refBioFragment;
import com.technologygroup.rayannoor.yoga.referees.refCertificateFragment;
import com.technologygroup.rayannoor.yoga.referees.refCourseFragment;
import com.technologygroup.rayannoor.yoga.referees.refEducationFragment;
import com.technologygroup.rayannoor.yoga.referees.refResumeFragment;

public class RefereeServicesPager extends FragmentStatePagerAdapter {


    public RefereeServicesPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                refResumeFragment res = new refResumeFragment();
                return res;

            case 1:
                refBioFragment bio = new refBioFragment();

                return bio;

            case 2:
                refCertificateFragment cer = new refCertificateFragment();

                return cer;

            case 3:
                refEducationFragment edu = new refEducationFragment();

                return edu;

            case 4:
                refCourseFragment course = new refCourseFragment();

                return course;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

}

