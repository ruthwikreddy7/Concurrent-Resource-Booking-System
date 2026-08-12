# Concurrent Booking System

A scalable booking system designed to handle **multiple users competing for a limited number of resources at the same time**.

The project focuses on building a reusable booking platform that can be applied to different real-world scenarios such as movie seats, railway tickets, event tickets, hotel rooms, appointment slots, and other limited-resource reservations.

---

## 📌 Overview

Many real-world booking platforms face the same fundamental challenge:

> **A large number of users may try to reserve a limited number of resources simultaneously.**

For example:

- Multiple users trying to book the same movie seat
- Large numbers of users attempting railway ticket bookings
- Users competing for limited event tickets
- Customers trying to reserve the same hotel room
- Users trying to obtain limited appointment slots
- Users competing for limited darshan slots

Although these applications belong to different domains, the underlying engineering problem is largely the same:

```text
                    Many Users
                        |
                        v
              Concurrent Requests
                        |
                        v
               Limited Resources
                        |
                        v
              Resource Allocation
                        |
                        v
                Successful Booking
