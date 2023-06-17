# Generated by Django 4.0.10 on 2023-06-17 09:50

import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Booking',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('hotel_id', models.IntegerField()),
                ('arrival', models.DateField()),
                ('departure', models.DateField()),
            ],
        ),
        migrations.CreateModel(
            name='Hotel',
            fields=[
                ('hotel_id', models.AutoField(primary_key=True, serialize=False)),
                ('title', models.CharField(max_length=100)),
                ('address', models.TextField()),
                ('price', models.DecimalField(decimal_places=2, max_digits=15)),
                ('rating', models.DecimalField(decimal_places=2, max_digits=3)),
                ('description', models.TextField()),
                ('amenities', models.TextField(blank=True, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='Review',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('hotel_id', models.IntegerField()),
                ('rating', models.IntegerField(validators=[django.core.validators.MinValueValidator(0), django.core.validators.MaxValueValidator(5)])),
                ('text', models.TextField()),
            ],
        ),
        migrations.DeleteModel(
            name='Product',
        ),
    ]