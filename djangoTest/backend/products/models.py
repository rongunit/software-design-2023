from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator


# Create your models here.
class Hotel(models.Model):
    hotel_id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    address = models.TextField()
    price = models.DecimalField(max_digits=15, decimal_places=2)
    rating = models.DecimalField(max_digits=3, decimal_places=2)
    description = models.TextField()
    amenities = models.TextField(blank=True, null=True)


class Review(models.Model):
    # hotel_id = models.ForeignKey(Hotel, on_delete=)
    hotel_id = models.IntegerField()
    rating = models.IntegerField(validators=[MinValueValidator(0), MaxValueValidator(5)])
    text = models.TextField()


class Booking(models.Model):
    # hotel_id =models.ForeignKey(Hotel,)
    hotel_id = models.IntegerField()
    arrival = models.DateField()
    departure = models.DateField()
