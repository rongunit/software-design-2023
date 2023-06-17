from rest_framework import serializers
from products.models import Hotel, Review, Booking


class HotelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Hotel
        fields = ['hotel_id', 'title', 'address', 'price', 'rating', 'description',
                  'amenities']


class ReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = Review
        fields = ['hotel_id', 'rating', 'text']


class BookingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Booking
        fields = ['hotel_id', 'arrival', 'departure']
