package com.sd.lib.swipemenu.utils;

import com.sd.lib.swipemenu.SwipeMenu;

import java.lang.ref.WeakReference;

public class SingleModeSwipeMenuHolder extends SwipeMenuHolder implements
        SwipeMenu.OnViewPositionChangeCallback,
        SwipeMenu.OnScrollStateChangeCallback,
        SwipeMenu.PullCondition
{
    private WeakReference<SwipeMenu> mBusySwipeMenu;

    @Override
    public void bind(SwipeMenu swipeMenu, Object tag)
    {
        super.bind(swipeMenu, tag);
        swipeMenu.setOnViewPositionChangeCallback(this);
        swipeMenu.setOnScrollStateChangeCallback(this);
        swipeMenu.setPullCondition(this);
    }

    @Override
    public void onStateChanged(boolean isOpened, SwipeMenu swipeMenu)
    {
        super.onStateChanged(isOpened, swipeMenu);
        if (isOpened)
            setAllSwipeMenuOpenedExcept(false, true, swipeMenu);
    }

    @Override
    public void onViewPositionChanged(boolean isDrag, SwipeMenu swipeMenu)
    {
        if (isDrag)
            setAllSwipeMenuOpenedExcept(false, true, swipeMenu);
    }

    private SwipeMenu getBusySwipeMenu()
    {
        return mBusySwipeMenu == null ? null : mBusySwipeMenu.get();
    }

    @Override
    public void onScrollStateChanged(SwipeMenu.ScrollState state, SwipeMenu swipeMenu)
    {
        final SwipeMenu busySwipeMenu = getBusySwipeMenu();
        if (state == SwipeMenu.ScrollState.Idle)
        {
            if (swipeMenu == busySwipeMenu)
                mBusySwipeMenu = null;
        } else
        {
            if (busySwipeMenu == null)
                mBusySwipeMenu = new WeakReference<>(swipeMenu);
        }
    }

    @Override
    public boolean canPull(SwipeMenu swipeMenu)
    {
        final SwipeMenu busySwipeMenu = getBusySwipeMenu();
        if (busySwipeMenu == null)
            return true;

        return swipeMenu == busySwipeMenu;
    }
}