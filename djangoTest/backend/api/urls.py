from django.urls import path

from . import views

urlpatterns = [
    path('hotels', views.hotels_list),
    path('hotels/<int:hotel_id>', views.hotels_detailed),
    path('bookings', views.bookings_add),
    path('reviews/<int:hotel_id>', views.reviews_list),
    path('reviews', views.reviews_add),
]
