package com.maxiaobu.healthclub.utils.rx;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;

import rx.Observable;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {

    private final Relay<Object, Object> _bus = PublishRelay.create().toSerialized();

    public void send(Object o) {
        _bus.call(o);
    }

    public Observable<Object> asObservable() {
        return _bus.asObservable();
    }

    /**
     * Relays are RxJava types which are both an Observable and an Action1.

     Basically: A Subject except without the ability to call onComplete or onError.

     Subjects are useful to bridge the gap between non-Rx APIs. However, they are stateful in a
     damaging way: when they receive an onComplete or onError they no longer become usable for moving
     data. This is the observable contract and sometimes it is the desired behavior. Most times it is not.

     Relays are simply Subjects without the aforementioned property. They allow you to bridge non-Rx
     APIs into Rx easily, and without the worry of accidentally triggering a terminal state.

     As more of your code moves to reactive, the need for Subjects and Relays should diminish. In the
     transitional period, or for quickly adapting a non-Rx API, Relays provide the convenience of Subjects
     without the worry of the statefulness of terminal event behavior.
     * @return
     */
    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
